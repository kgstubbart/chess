package chess.movement;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovement extends BaseMovementRule {
    public PawnMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            calculateMoves(board, position, -1, 0, moves, false);
        }
        else {
            calculateMoves(board, position, 1, 0, moves, false);
        }
        return moves;
    }
}
