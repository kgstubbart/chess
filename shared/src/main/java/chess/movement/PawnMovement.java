package chess.movement;
import chess.*;
import java.util.Collection;
import java.util.HashSet;

public class PawnMovement extends BaseMovement {
    public PawnMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        int row = position.getRow();
        int col = position.getColumn();
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

        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (tempRow + 1 <= 8) {
                if (board.getPiece(new ChessPosition(tempRow + 1, tempCol)) == null) {
                    if (tempRow + 1 == 8) {
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol), ChessPiece.PieceType.KNIGHT));
                    } else {
                        baseCalc(board, position, -1, 0, moves, false);
                    }
                    if ((row == 6) && (board.getPiece(new ChessPosition(tempRow + 2, tempCol)) == null)) {
                        baseCalc(board, position, -2, 0, moves, false);
                    }
                }
            }
            if (tempRow + 1 <= 8 && tempCol + 1 <= 8) {
                if (board.getPiece(new ChessPosition(tempRow + 1, tempCol + 1)) != null) {
                    if (tempRow + 1 == 8) {
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol + 1), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol + 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol + 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol + 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        baseCalc(board, position, -1, 1, moves, false);
                    }
                }
            }
            if (tempRow + 1 <= 8 && tempCol - 1 >= 1) {
                if (board.getPiece(new ChessPosition(tempRow + 1, tempCol - 1)) != null) {
                    if (tempRow + 1 == 8) {
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol - 1), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol - 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol - 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow + 1, tempCol - 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        baseCalc(board, position, -1, -1, moves, false);
                    }
                }
            }
        } else {
            if (tempRow - 1 >= 1) {
                if (board.getPiece(new ChessPosition(tempRow - 1, tempCol)) == null) {
                    if (tempRow - 1 == 1) {
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol), ChessPiece.PieceType.KNIGHT));
                    } else {
                        baseCalc(board, position, 1, 0, moves, false);
                    }
                    if ((row == 1) && (board.getPiece(new ChessPosition(tempRow - 2, tempCol)) == null)) {
                        baseCalc(board, position, 2, 0, moves, false);
                    }
                }
            }
            if (tempRow - 1 >= 1 && tempCol + 1 <= 8) {
                if (board.getPiece(new ChessPosition(tempRow - 1, tempCol + 1)) != null) {
                    if (tempRow - 1 == 1) {
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol + 1), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol + 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol + 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol + 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        baseCalc(board, position, 1, 1, moves, false);
                    }
                }
            }
            if (tempRow - 1 >= 1 && tempCol - 1 >= 1) {
                if (board.getPiece(new ChessPosition(tempRow - 1, tempCol - 1)) != null) {
                    if (tempRow - 1 == 1) {
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol - 1), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol - 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol - 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(tempRow - 1, tempCol - 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        baseCalc(board, position, 1, -1, moves, false);
                    }
                }
            }
        }
        return moves;
    }
}
