package chess.movement;

import chess.movement.PieceMovementCalculator;
import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;
import java.util.Collection;

public abstract class BaseMovementRule implements PieceMovementCalculator{

    private final ChessBoard board;
    private final ChessPosition position;

    public BaseMovementRule(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {

        int row = pos.getRow();
        int col = pos.getColumn();

        if (allowDistance) {
            while ((row >= 0) && (row <= 7) && (col >= 0) && (col <= 7)) {
                if ((row == 0 && rowInc < 0) || (row == 7 && rowInc > 0) || (col == 0 && colInc < 0) || (col == 7 && colInc > 0)) {
                    break;
                }
                else {
                    row += rowInc;
                    col += colInc;

                    int temp_row = switch (row) {
                        case 7 -> 1;
                        case 6 -> 2;
                        case 5 -> 3;
                        case 4 -> 4;
                        case 3 -> 5;
                        case 2 -> 6;
                        case 1 -> 7;
                        case 0 -> 8;
                        default -> row;
                    };
                    int temp_col = col + 1;

                    ChessPosition position = new ChessPosition(temp_row, temp_col);
                    if (board.getPiece(position) != null) {
                        if (board.getPiece(position).getTeamColor() != board.getPiece(pos).getTeamColor()) {
                            moves.add(new ChessMove(pos, position, null));
                        }
                        break;
                    } else {
                        moves.add(new ChessMove(pos, position, null));
                    }
                }
            }
        }
        else {
            if ((row == 0 && rowInc < 0) || (row == 7 && rowInc > 0) || (col == 0 && colInc < 0) || (col == 7 && colInc > 0)) {
                // do nothing
            }
            else {
                row += rowInc;
                col += colInc;

                if (row < 0 || row > 7 || col < 0 || col > 7) {
                    // do nothing
                }
                else {
                    int temp_row = switch (row) {
                        case 7 -> 1;
                        case 6 -> 2;
                        case 5 -> 3;
                        case 4 -> 4;
                        case 3 -> 5;
                        case 2 -> 6;
                        case 1 -> 7;
                        case 0 -> 8;
                        default -> row;
                    };
                    int temp_col = col + 1;

                    ChessPosition position = new ChessPosition(temp_row, temp_col);
                    if (board.getPiece(position) != null) {
                        if (board.getPiece(position).getTeamColor() != board.getPiece(pos).getTeamColor()) {
                            moves.add(new ChessMove(pos, position, null));
                        }
                    } else {
                        moves.add(new ChessMove(pos, position, null));
                    }
                }
            }
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}
