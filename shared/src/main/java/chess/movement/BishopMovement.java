package chess.movement;

import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class BishopMovement extends BaseMovementRule {
    public BishopMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, position, -1, -1, moves, true);
        calculateMoves(board, position, 1, -1, moves, true);
        calculateMoves(board, position, -1, 1, moves, true);
        calculateMoves(board, position, 1, 1, moves, true);
        return moves;
    }
}
