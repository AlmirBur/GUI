package othello.model;

public class Cell {
    public static final int size = 50;
    private ChipValue value;
    private boolean emptyForMove;

    public ChipValue getValue() {
        return value;
    }

    public void setValue(ChipValue value) {
        this.value = value;
    }

    public boolean isFreeForMove() {
        return emptyForMove;
    }

    public void setIsFreeForMove(boolean value) {
        emptyForMove = value;
    }
}