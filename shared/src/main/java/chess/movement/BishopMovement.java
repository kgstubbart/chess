package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovement extends BaseMovement {
    public BishopMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        baseCalc(board, position, 1, 1, moves, true);
        baseCalc(board, position, 1, -1, moves, true);
        baseCalc(board, position, -1, 1, moves, true);
        baseCalc(board, position, -1, -1, moves, true);
        return moves;
    }
}
