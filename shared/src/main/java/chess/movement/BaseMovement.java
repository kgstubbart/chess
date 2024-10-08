package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BaseMovement {
    private final ChessBoard board;
    private final ChessPosition pos;

    public BaseMovement(ChessBoard board, ChessPosition pos) {
        this.board = board;
        this.pos = pos;
    }

    protected void BaseCalc(ChessBoard board, ChessPosition pos, int rowInc, int colInc,
                            Collection<ChessMove> moves, boolean allowDistance) {
        if (allowDistance) {
            int row = pos.getRow();
            int col = pos.getColumn();

            while (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
                row += rowInc;
                col += colInc;

                if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
                    int temp_col = col + 1;
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

                    if (board.getPiece(new ChessPosition(temp_row, temp_col)) == null) {
                        moves.add(new ChessMove(pos, new ChessPosition(temp_row, temp_col), null));
                    }
                    else {
                        if (board.getPiece(new ChessPosition(temp_row, temp_col)).getTeamColor() != board.getPiece(pos).getTeamColor()) {
                            moves.add(new ChessMove(pos, new ChessPosition(temp_row, temp_col), null));
                            break;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        else {
            int row = pos.getRow();
            int col = pos.getColumn();

            row += rowInc;
            col += colInc;

            if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
                int temp_col = col + 1;
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

                if (board.getPiece(new ChessPosition(temp_row, temp_col)) == null) {
                    moves.add(new ChessMove(pos, new ChessPosition(temp_row, temp_col), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(temp_row, temp_col)).getTeamColor() != board.getPiece(pos).getTeamColor()) {
                        moves.add(new ChessMove(pos, new ChessPosition(temp_row, temp_col), null));
                    }
                }
            }
        }
    }
}
