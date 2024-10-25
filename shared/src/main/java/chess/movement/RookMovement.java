package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovement extends BaseMovement {
    public RookMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        baseCalc(board, position, 1, 0, moves, true);
        baseCalc(board, position, 0, 1, moves, true);
        baseCalc(board, position, -1, 0, moves, true);
        baseCalc(board, position, 0, -1, moves, true);
        return moves;
    }
}
