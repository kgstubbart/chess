package chess.movement;

import chess.movement.PieceMovementCalculator;
import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;
import java.util.Collection;

public abstract class BaseMovementRule implements PieceMovementCalculator{

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        int row = pos.getRow();
        int col = pos.getColumn();

        if (allowDistance) {
            while (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                int updated_row = row + rowInc;
                int updated_col = col + colInc;
                ChessPosition position = new ChessPosition(updated_row, updated_col);
                if (board.getPiece(position) != null) {
                    if (board.getPiece(position).getTeamColor() != board.getPiece(pos).getTeamColor()) {
                        moves.add(new ChessMove(pos, position, null));
                    }
                    break;
                }
                else {
                    moves.add(new ChessMove(pos, position, null));
                }
            }
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
