package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovement extends BaseMovement {
    public KnightMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        baseCalc(board, position, 2, 1, moves, false);
        baseCalc(board, position, 1, 2, moves, false);
        baseCalc(board, position, -2, 1, moves, false);
        baseCalc(board, position, 1, -2, moves, false);
        baseCalc(board, position, 2, -1, moves, false);
        baseCalc(board, position, -1, 2, moves, false);
        baseCalc(board, position, -2, -1, moves, false);
        baseCalc(board, position, -1, -2, moves, false);
        return moves;
    }
}
