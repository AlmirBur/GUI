package othello.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private Cell[][] cells = new Cell[8][8];

    public Field() {
        restart();
    }

    public void restart() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell();
            }
        }
        cells[3][4].setValue(ChipValue.BLACK);
        cells[4][3].setValue(ChipValue.BLACK);
        cells[3][3].setValue(ChipValue.WHITE);
        cells[4][4].setValue(ChipValue.WHITE);
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) throw new IllegalArgumentException();
        return cells[x][y];
    }

    public boolean[][] getFreeCells(Player player) {
        boolean[][] freeCells = new boolean[8][8];
        boolean value;
        player.setCanMove(false);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                value = (trueDirections(i, j, player) != null);
                freeCells[i][j] = value;
                cells[i][j].setIsFreeForMove(value);
                if (value) player.setCanMove(true);
            }
        }
        return freeCells;
    }

    private List<Boolean> trueDirections(int x, int y, Player player) {//null при всех false или непустой клетке
        if (cells[x][y].getValue() != null) return null;
        List<Boolean> directions = new ArrayList<>();
        int i;
        int j;
        int dx;
        int dy;
        int X1;
        int X2;
        int X3;
        int notX1;
        int notX2;
        int notX3;
        int sum;
        boolean lastChip;
        for (int k = 0; k < 8; k++) {
            lastChip = false;
            sum = 0;
            X1 = k / 4;
            X2 = (k / 2) % 2;
            X3 = k % 2;
            notX1 = X1 ^ 1;
            notX2 = X2 ^ 1;
            notX3 = X3 ^ 1;
            dx = -1 * (notX1 * notX3 | notX1 * notX2) | X1 * X3 | X1 * X2;
            dy = -1 * (notX1 * notX2 * notX3 | notX1 * X2 * X3 | X1 * notX2 * X3)
                    + (notX1 * X2 * notX3 | X1 * notX2 * notX3 | X1 * X2 * X3);
            i = x + dx;
            j = y + dy;
            while (i >= 0 && i < 8 && j >= 0 && j < 8) {
                if (cells[i][j].getValue() == null) break;
                if (cells[i][j].getValue() == player.getChip()) {
                    lastChip = true;
                    break;
                }else {
                    sum++;
                }
                i += dx;
                j += dy;
            }
            directions.add(lastChip && sum > 0);
        }
        if (directions.contains(true)) return directions;
        else return null;
    }

    public void putChip(int x, int y, Player player) {
        List<Boolean> directions = trueDirections(x, y, player);
        if (directions == null) throw new IllegalArgumentException();
        cells[x][y].setValue(player.getChip());
        int i;
        int j;
        int dx;
        int dy;
        int X1;
        int X2;
        int X3;
        int notX1;
        int notX2;
        int notX3;
        for (int k = 0; k < 8; k++) {
            if (!directions.get(k)) continue;
            X1 = k / 4;
            X2 = (k / 2) % 2;
            X3 = k % 2;
            notX1 = X1 ^ 1;
            notX2 = X2 ^ 1;
            notX3 = X3 ^ 1;
            dx = -1 * (notX1 * notX3 | notX1 * notX2) | X1 * X3 | X1 * X2;
            dy = -1 * (notX1 * notX2 * notX3 | notX1 * X2 * X3 | X1 * notX2 * X3)
                    + (notX1 * X2 * notX3 | X1 * notX2 * notX3 | X1 * X2 * X3);
            i = x + dx;
            j = y + dy;
            while (cells[i][j].getValue() != player.getChip()) {
                cells[i][j].setValue(player.getChip());
                i += dx;
                j += dy;
            }
        }
    }
}