package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    public ChessPosition(int row, int col) {
        this.col = col - 1;
        int temp_row = row;
        switch(row) {
            case 1:
                temp_row = 7;
                break;
            case 2:
                temp_row = 6;
                break;
            case 3:
                temp_row = 5;
                break;
            case 4:
                temp_row = 4;
                break;
            case 5:
                temp_row = 3;
                break;
            case 6:
                temp_row = 2;
                break;
            case 7:
                temp_row = 1;
                break;
            case 8:
                temp_row = 0;
                break;
        }
        this.row = temp_row;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}
