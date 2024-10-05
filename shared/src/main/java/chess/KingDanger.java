package chess;

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

        // check diagonal
        List<Integer> diagonal_row_Incs = List.of(1, 1, -1, -1);
        List<Integer> diagonal_col_Incs = List.of(1, -1, 1, -1);
        for (int i = 0; i <= 3; i++) {
            int diagonal_row = temp_row;
            int diagonal_col = temp_col;
            while (diagonal_row >= 1 && diagonal_row <= 8 && diagonal_col >= 1 && diagonal_col <= 8) {
                int rowInc = diagonal_row_Incs.get(i);
                int colInc = diagonal_col_Incs.get(i);
                diagonal_row += rowInc;
                diagonal_col += colInc;
                if (diagonal_row < 1 || diagonal_row > 8 || diagonal_col < 1 || diagonal_col > 8) {
                    break;
                }
                ChessPiece piece = board.getPiece(new ChessPosition(diagonal_row, diagonal_col));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor
                            && (piece.getPieceType() == ChessPiece.PieceType.BISHOP ||
                            piece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        // check straight
        List<Integer> straight_row_Incs = List.of(1, -1, 0, 0);
        List<Integer> straight_col_Incs = List.of(0, 0, 1, -1);
        for (int i = 0; i <= 3; i++) {
            int straight_row = temp_row;
            int straight_col = temp_col;
            while (straight_row >= 1 && straight_row <= 8 && straight_col >= 1 && straight_col <= 8) {
                int rowInc = straight_row_Incs.get(i);
                int colInc = straight_col_Incs.get(i);
                straight_row += rowInc;
                straight_col += colInc;
                if (straight_row < 1 || straight_row > 8 || straight_col < 1 || straight_col > 8) {
                    break;
                }
                ChessPiece piece = board.getPiece(new ChessPosition(straight_row, straight_col));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor
                            && (piece.getPieceType() == ChessPiece.PieceType.ROOK ||
                            piece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        // check knight
        List<Integer> knight_row_Incs = List.of(2, 1, -2, 1, 2, -1, -2, -1);
        List<Integer> knight_col_Incs = List.of(1, 2, 1, -2, -1, 2, -1, -2);
        for (int i = 0; i <= 7; i++) {
            int knight_row = temp_row + knight_row_Incs.get(i);
            int knight_col = temp_col + knight_col_Incs.get(i);
            if (knight_row >= 1 && knight_row <= 8 && knight_col >= 1 && knight_col <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(knight_row, knight_col));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor
                            && (piece.getPieceType() == ChessPiece.PieceType.KNIGHT)) {
                        return true;
                    }
                }
            }
        }

        // check for king


        if (teamColor == ChessGame.TeamColor.WHITE) {
            // check for king (may be able to do pawn here too)

            // check for pawn

        }
        else {
            // check for king (may be able to do pawn here too)

            // check for pawn

        }
        return false;
    }
}
