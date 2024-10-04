package chess;

public class KingDanger {
    private final ChessBoard board;
    private final ChessPosition pos;
    private final ChessGame.TeamColor teamColor;

    public KingDanger(ChessBoard board, ChessPosition pos, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.pos = pos;
        this.teamColor = teamColor;
    }

    protected boolean BaseDanger(ChessBoard board, ChessPosition pos, ChessGame.TeamColor teamColor) {
        int row = pos.getRow();
        int col = pos.getColumn();

        int temp_col = col + 1;
        int temp_row = switch (row) {
            case 7 -> 1;
            case 6 -> 2;
            case 5 -> 3;
            case 4 -> 4;
            case 3 -> 5;
            case 2 -> 6;
            case 1 -> 7;
            case 0 -> 8;
            default -> row;
        };

        if (teamColor == ChessGame.TeamColor.WHITE) {
            // check for king (may be able to do pawn here too)

            // check for pawn

            // check for knight

            // check diagonal

            // check straight
        }
        else {
            // check for king (may be able to do pawn here too)

            // check for pawn

            // check for knight

            // check diagonal

            // check straight
        }
        return false;
    }
}
