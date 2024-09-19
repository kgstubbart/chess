package chess.movement;

import java.util.Collection;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;


public interface PieceMovementCalculator {
    Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
