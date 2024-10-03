package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMovement extends BaseMovement {
    public KingMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        BaseCalc(board, position, 1, 1, moves, false);
        BaseCalc(board, position, 1, -1, moves, false);
        BaseCalc(board, position, -1, 1, moves, false);
        BaseCalc(board, position, -1, -1, moves, false);
        BaseCalc(board, position, 1, 0, moves, false);
        BaseCalc(board, position, 0, 1, moves, false);
        BaseCalc(board, position, -1, 0, moves, false);
        BaseCalc(board, position, 0, -1, moves, false);
        return moves;
    }
}
