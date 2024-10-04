package chess;

import java.util.ArrayList;
import java.util.List;

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
            int diagonal_row = temp_row;
            int diagonal_col = temp_col;

            List<Integer> row_Incs = List.of(1, 1, -1, -1);
            List<Integer> col_Incs = List.of(1, -1, 1, -1);


            for (int i = 0; i <= 3; i++) {
                while (diagonal_row >= 1 && diagonal_row <= 8 && diagonal_col >= 1 && diagonal_col <= 8) {
                    int rowInc = row_Incs.get(i);
                    int colInc = row_Incs.get(i);
                    diagonal_row += rowInc;
                    diagonal_col += colInc;

                    if (board.getPiece(new ChessPosition(diagonal_row, diagonal_col)) != null) {
                        if (board.getPiece(new ChessPosition(diagonal_row, diagonal_col)).getTeamColor() == ChessGame.TeamColor.BLACK
                                && (board.getPiece(new ChessPosition(diagonal_row, diagonal_col)).getPieceType() == ChessPiece.PieceType.BISHOP ||
                                board.getPiece(new ChessPosition(diagonal_row, diagonal_col)).getPieceType() == ChessPiece.PieceType.QUEEN)) {
                            return true;
                        } else {
                            break;
                        }
                    }
                }
            }
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
