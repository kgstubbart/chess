package chess.movement;

import chess.movement.PieceMovementCalculator;
import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;
import java.util.Collection;

public abstract class BaseMovementRule implements PieceMovementCalculator{

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        // Generic code for calculating most piece rules
        // ...
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}

