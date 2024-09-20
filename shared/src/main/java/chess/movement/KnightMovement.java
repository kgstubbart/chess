package chess.movement;

import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class KnightMovement extends BaseMovementRule {
    public KnightMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, position, 2, 1, moves, false);
        calculateMoves(board, position, 2, -1, moves, false);
        calculateMoves(board, position, -2, 1, moves, false);
        calculateMoves(board, position, -2, -1, moves, false);
        calculateMoves(board, position, 1, 2, moves, false);
        calculateMoves(board, position, -1, 2, moves, false);
        calculateMoves(board, position, 1, -2, moves, false);
        calculateMoves(board, position, -1, -2, moves, false);
        return moves;
    }
}
