package othello.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Group;
import javafx.stage.Modality;
import javafx.stage.Stage;
import othello.model.Cell;
import othello.model.ChipValue;
import othello.model.Field;
import othello.model.Player;

import java.io.FileInputStream;

public class View extends Application {
    private Stage main;
    private Group chips;
    private Group hints;
    private Field field = new Field();
    private Player player;

    private int x0 = 10, y0 = 10;
    private int xEnd = 410, yEnd = 410;

    private Image imgBlackChip;
    private Image imgWhiteChip;

    private Text numBlackChipsText;
    private Text numWhiteChipsText;
    private ImageView blackMarkerChip;
    private ImageView whiteMarkerChip;

    @Override
    public void start(Stage stage) throws Exception{
        player = new Player();

        chips = new Group();

        hints = new Group();

        Group root = new Group();

        Scene scene = new Scene(root, 420, 500);
        scene.setFill(Color.rgb(100, 150, 200));

        main = stage;
        main.setTitle("Othello");
        main.setScene(scene);
        main.setResizable(false);

        Button restart = new Button("restart");
        restart.setLayoutX(360);
        restart.setLayoutY(465);
        restart.setOnMouseClicked(event -> restart());

        imgBlackChip = new Image(new FileInputStream("files/blackChip.png"));
        imgWhiteChip = new Image(new FileInputStream("files/whiteChip.png"));
        ImageView fieldView = new ImageView(new Image(new FileInputStream("files/field.png")));
        fieldView.setX(x0);
        fieldView.setY(y0);

        blackMarkerChip = new ImageView(imgBlackChip);
        blackMarkerChip.setX(20);
        blackMarkerChip.setY(430);

        whiteMarkerChip = new ImageView(imgWhiteChip);
        whiteMarkerChip.setX(170);
        whiteMarkerChip.setY(430);

        numBlackChipsText = new Text();
        numBlackChipsText.setFont(new Font(40));
        numBlackChipsText.setX(100);
        numBlackChipsText.setY(470);

        numWhiteChipsText = new Text();
        numWhiteChipsText.setFont(new Font(40));
        numWhiteChipsText.setX(250);
        numWhiteChipsText.setY(470);

        root.getChildren().addAll(fieldView, chips, hints, restart, blackMarkerChip, whiteMarkerChip, numBlackChipsText, numWhiteChipsText);

        restart();

        main.show();

        scene.setOnMouseClicked(event -> {
            if (event.getSceneX() < x0 || event.getSceneX() >= xEnd ||
                event.getSceneY() < y0 || event.getSceneY() >= yEnd) return;
            int x = (int) (event.getSceneX() - x0) / Cell.size;
            int y = (int) (event.getSceneY() - y0) / Cell.size;
            if (!field.getCell(x, y).isFreeForMove()) return;
            field.putChip(x, y, player);
            repaintField();
            numBlackChipsText.setText(String.valueOf(numChips(ChipValue.BLACK)));
            numWhiteChipsText.setText(String.valueOf(numChips(ChipValue.WHITE)));
            player.changePlayer();
            showHints();
        });
    }

    private int numChips(ChipValue chipValue) {
        int num = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field.getCell(i, j).getValue() == chipValue) num++;
            }
        }
        return num;
    }

    private void restart() {
        chips.getChildren().remove(0, chips.getChildren().size());
        player.setChip(ChipValue.BLACK);
        field.restart();
        numBlackChipsText.setText(String.valueOf(numChips(ChipValue.BLACK)));
        numWhiteChipsText.setText(String.valueOf(numChips(ChipValue.WHITE)));
        repaintField();
        showHints();
    }

    private void repaintField() {
        chips.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field.getCell(i, j).getValue() == null) continue;
                ImageView chipView;
                if (field.getCell(i, j).getValue() == ChipValue.BLACK) {
                    chipView = new ImageView(imgBlackChip);
                }
                else{
                    chipView = new ImageView(imgWhiteChip);
                }
                chipView.setX(i * Cell.size + x0);
                chipView.setY(j * Cell.size + y0);
                chips.getChildren().add(chipView);
            }
        }
    }

    private void showHints() {
        hints.getChildren().clear();
        boolean[][] emptyCells = field.getFreeCells(player);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!emptyCells[i][j]) continue;
                Circle hint = new Circle(i * Cell.size + x0 + 25, j * Cell.size + y0 + 25, 7);
                if (player.getChip() == ChipValue.BLACK) {
                    hint.setFill(Color.gray(0));
                }else {
                    hint.setFill(Color.gray(1));
                }
                hints.getChildren().add(hint);
            }
        }
        if (player.getChip() == ChipValue.BLACK) {
            blackMarkerChip.setFitWidth(80);
            blackMarkerChip.setFitHeight(80);
            blackMarkerChip.setX(5);
            blackMarkerChip.setY(415);
            whiteMarkerChip.setFitWidth(50);
            whiteMarkerChip.setFitHeight(50);
            whiteMarkerChip.setX(170);
            whiteMarkerChip.setY(430);
        } else{
            blackMarkerChip.setFitWidth(50);
            blackMarkerChip.setFitHeight(50);
            blackMarkerChip.setX(20);
            blackMarkerChip.setY(430);
            whiteMarkerChip.setFitWidth(80);
            whiteMarkerChip.setFitHeight(80);
            whiteMarkerChip.setX(155);
            whiteMarkerChip.setY(415);
        }
        if (player.canMove()) {
            if (player.isChanged()) {
                player.setIsChanged(false);
            }
        } else{
            if (player.isChanged()) {
                player.setIsChanged(false);
                String message;
                int numBlackChips = numChips(ChipValue.BLACK);
                int numWhiteChips = numChips(ChipValue.WHITE);
                if (numBlackChips == numWhiteChips) {
                    message = "Ничья";
                } else{
                    if (numBlackChips > numWhiteChips) {
                        message = "Выиграли чёрные";
                    } else{
                        message = "Выиграли белые";
                    }
                }
                situation(message);
                restart();
            } else{
                player.changePlayer();
                player.setIsChanged(true);
                situation("Ход передаётся следующему игроку");
                showHints();
            }
        }
    }

    private void situation(String message) {
        Group situationRoot = new Group();

        Scene scene = new Scene(situationRoot);

        Stage modal = new Stage();
        modal.setScene(scene);
        modal.initOwner(main);
        modal.setResizable(false);
        modal.initModality(Modality.APPLICATION_MODAL);

        Text messageText = new Text(message);
        messageText.setFont(new Font(20));
        messageText.setX(0);
        messageText.setY(20);
        situationRoot.getChildren().addAll(messageText);

        modal.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}