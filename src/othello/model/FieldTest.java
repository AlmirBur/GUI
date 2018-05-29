package othello.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    @Test
    void tests() {
        Field field = new Field();

        field.restart();
        assertEquals(ChipValue.BLACK, field.getCell(3, 4).getValue());
        assertEquals(ChipValue.BLACK, field.getCell(4, 3).getValue());
        assertEquals(ChipValue.WHITE, field.getCell(3, 3).getValue());
        assertEquals(ChipValue.WHITE, field.getCell(4, 4).getValue());
        assertEquals(null, field.getCell(0, 0).getValue());
        assertEquals(null, field.getCell(7, 7).getValue());
        assertThrows(IllegalArgumentException.class, () -> field.getCell(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> field.getCell(8, 8));

        Player player = new Player();
        player.setChip(ChipValue.BLACK);
        boolean[][] freeCells = field.getFreeCells(player);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 2 && j == 3 || i == 3 && j == 2 || i == 4 && j == 5 || i == 5 && j == 4) {
                    assertEquals(true, freeCells[i][j]);
                }else {
                    assertEquals(false, freeCells[i][j]);
                }
            }
        }

        field.putChip(2, 3, player);
        assertEquals(ChipValue.BLACK, field.getCell(2, 3).getValue());
        assertEquals(ChipValue.BLACK, field.getCell(3, 4).getValue());
        assertEquals(ChipValue.BLACK, field.getCell(4, 3).getValue());
        assertEquals(ChipValue.BLACK, field.getCell(3, 3).getValue());
        assertEquals(ChipValue.WHITE, field.getCell(4, 4).getValue());

        player.changePlayer();
        field.getFreeCells(player);
        field.putChip(2, 4, player);
        assertEquals(ChipValue.BLACK, field.getCell(2, 3).getValue());
        assertEquals(ChipValue.BLACK, field.getCell(4, 3).getValue());
        assertEquals(ChipValue.BLACK, field.getCell(3, 3).getValue());
        assertEquals(ChipValue.WHITE, field.getCell(2, 4).getValue());
        assertEquals(ChipValue.WHITE, field.getCell(3, 4).getValue());
        assertEquals(ChipValue.WHITE, field.getCell(4, 4).getValue());
    }
}