package othello.model;

public class Player {
    private ChipValue chip;
    private boolean playerIsChanged = false;
    private boolean playerCanMove;

    public void changePlayer() {
        if (chip == ChipValue.BLACK) chip = ChipValue.WHITE;
        else chip = ChipValue.BLACK;
    }

    public ChipValue getChip() {
        return chip;
    }

    public void setChip(ChipValue chip) {
        this.chip = chip;
    }

    public boolean isChanged() {
        return playerIsChanged;
    }

    public void setIsChanged(boolean playerIsChanged) {
        this.playerIsChanged = playerIsChanged;
    }

    public boolean canMove() {
        return playerCanMove;
    }

    public void setCanMove(boolean value) {
        this.playerCanMove = value;
    }
}