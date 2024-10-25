package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    public static ChessBoard copyOf(ChessBoard board) {
        ChessBoard boardCopy = new ChessBoard();
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                ChessPiece piece = board.squares[row][col];
                if (piece != null) {
                    boardCopy.squares[row][col] = piece.clone();
                }
            }
        }
        return boardCopy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];

        for (int col = 0; col <= 7; col++) {
            squares[6][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        for (int col = 0; col <= 7; col++) {
            squares[1][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

        for (int col = 0; col <= 7; col++) {
            if (col == 0 || col == 7) {
                squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            }
            if (col == 1 || col == 6) {
                squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            }
            if (col == 2 || col == 5) {
                squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            }
            if (col == 3) {
                squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
            }
            if (col == 4) {
                squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
            }
        }

        for (int col = 0; col <= 7; col++) {
            if (col == 0 || col == 7) {
                squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            }
            if (col == 1 || col == 6) {
                squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            }
            if (col == 2 || col == 5) {
                squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            }
            if (col == 3) {
                squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            }
            if (col == 4) {
                squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
            }
        }
    }
}
