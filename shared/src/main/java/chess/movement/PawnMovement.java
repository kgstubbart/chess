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
        int row = position.getRow();
        int col = position.getColumn();

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

        var moves = new HashSet<ChessMove>();
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition forward_position = new ChessPosition(temp_row + 1, temp_col);
            if (board.getPiece(forward_position) == null) {
                calculateMoves(board, position, -1, 0, moves, false);
                if (row == 6) {
                    ChessPosition double_forward_position = new ChessPosition(temp_row + 2, temp_col);
                    if (board.getPiece(double_forward_position) == null) {
                        calculateMoves(board, position, -2, 0, moves, false);
                    }
                }
            }
            ChessPosition right_attack_position = new ChessPosition(temp_row + 1, temp_col - 1);
            if (board.getPiece(right_attack_position) != null) {
                calculateMoves(board, position, -1, -1, moves, false);
            }
            ChessPosition left_attack_position = new ChessPosition(temp_row - 1, temp_col - 1);
            if (board.getPiece(left_attack_position) != null) {
                calculateMoves(board, position, 1, -1, moves, false);
            }
        }
        else {
            ChessPosition forward_position = new ChessPosition(temp_row - 1, temp_col);
            if (board.getPiece(forward_position) == null) {
                calculateMoves(board, position, 1, 0, moves, false);
                if (row == 1) {
                    ChessPosition double_forward_position = new ChessPosition(temp_row - 2, temp_col);
                    if (board.getPiece(double_forward_position) == null) {
                        calculateMoves(board, position, 2, 0, moves, false);
                    }
                }
            }
            ChessPosition left_attack_position = new ChessPosition(temp_row + 1, temp_col + 1);
            if (board.getPiece(left_attack_position) != null) {
                calculateMoves(board, position, -1, 1, moves, false);
            }
            ChessPosition right_attack_position = new ChessPosition(temp_row - 1, temp_col + 1);
            if (board.getPiece(right_attack_position) != null) {
                calculateMoves(board, position, 1, 1, moves, false);
            }
        }
        return moves;
    }
}
