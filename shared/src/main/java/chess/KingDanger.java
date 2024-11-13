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

    protected boolean pawnCheck(int tempRow, int tempCol, List<Integer> pawnRowIncs, List<Integer> pawnColIncs) {
        boolean result = false;
        for (int i = 0; i <= 1; i++) {
            int pawnRow = tempRow + pawnRowIncs.get(i);
            int pawnCol = tempCol + pawnColIncs.get(i);
            if (pawnRow >= 1 && pawnRow <= 8 && pawnCol >= 1 && pawnCol <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(pawnRow, pawnCol));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor && (piece.getPieceType() == ChessPiece.PieceType.PAWN)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    protected boolean baseDanger(ChessBoard board, ChessPosition pos, ChessGame.TeamColor teamColor) {
        int row = pos.getRow();
        int col = pos.getColumn();

        int tempCol = col + 1;
        int tempRow = switch (row) {
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
        List<Integer> diagonalRowIncs = List.of(1, 1, -1, -1);
        List<Integer> diagonalColIncs = List.of(1, -1, 1, -1);
        for (int i = 0; i <= 3; i++) {
            int diagonalRow = tempRow;
            int diagonalCol = tempCol;
            while (diagonalRow >= 1 && diagonalRow <= 8 && diagonalCol >= 1 && diagonalCol <= 8) {
                int rowInc = diagonalRowIncs.get(i);
                int colInc = diagonalColIncs.get(i);
                diagonalRow += rowInc;
                diagonalCol += colInc;
                if (diagonalRow < 1 || diagonalRow > 8 || diagonalCol < 1 || diagonalCol > 8) {
                    break;
                }
                ChessPiece piece = board.getPiece(new ChessPosition(diagonalRow, diagonalCol));
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
        List<Integer> straightRowIncs = List.of(1, -1, 0, 0);
        List<Integer> straightColIncs = List.of(0, 0, 1, -1);
        for (int i = 0; i <= 3; i++) {
            int straightRow = tempRow;
            int straightCol = tempCol;
            while (straightRow >= 1 && straightRow <= 8 && straightCol >= 1 && straightCol <= 8) {
                int rowInc = straightRowIncs.get(i);
                int colInc = straightColIncs.get(i);
                straightRow += rowInc;
                straightCol += colInc;
                if (straightRow < 1 || straightRow > 8 || straightCol < 1 || straightCol > 8) {
                    break;
                }
                ChessPiece piece = board.getPiece(new ChessPosition(straightRow, straightCol));
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
        List<Integer> knightRowIncs = List.of(2, 1, -2, 1, 2, -1, -2, -1);
        List<Integer> knightColIncs = List.of(1, 2, 1, -2, -1, 2, -1, -2);
        for (int i = 0; i <= 7; i++) {
            int knightRow = tempRow + knightRowIncs.get(i);
            int knightCol = tempCol + knightColIncs.get(i);
            if (knightRow >= 1 && knightRow <= 8 && knightCol >= 1 && knightCol <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(knightRow, knightCol));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor
                            && (piece.getPieceType() == ChessPiece.PieceType.KNIGHT)) {
                        return true;
                    }
                }
            }
        }

        // check for king
        List<Integer> kingRowIncs = List.of(1, 1, -1, -1, 1, 0, -1, 0);
        List<Integer> kingColIncs = List.of(1, -1, 1, -1, 0, 1, 0, -1);
        for (int i = 0; i <= 7; i++) {
            int kingRow = tempRow + kingRowIncs.get(i);
            int kingCol = tempCol + kingColIncs.get(i);
            if (kingRow >= 1 && kingRow <= 8 && kingCol >= 1 && kingCol <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(kingRow, kingCol));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor
                            && (piece.getPieceType() == ChessPiece.PieceType.KING)) {
                        return true;
                    }
                }
            }
        }

        // check for pawn
        if (teamColor == ChessGame.TeamColor.WHITE) {
            List<Integer> pawnRowIncs = List.of(1, 1);
            List<Integer> pawnColIncs = List.of(1, -1);
            boolean firstPawnResult = pawnCheck(tempRow, tempCol, pawnRowIncs, pawnColIncs);
            if (firstPawnResult) {
                return true;
            }
        }
        else {
            List<Integer> pawnRowIncs = List.of(-1, -1);
            List<Integer> pawnColIncs = List.of(1, -1);
            boolean secondPawnResult = pawnCheck(tempRow, tempCol, pawnRowIncs, pawnColIncs);
            if (secondPawnResult) {
                return true;
            }
        }
        return false;
    }
}
