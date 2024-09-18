package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
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
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                squares[position.getRow()][position.getColumn()] = null;
            }
        }
        for (int j = 1; j <= 8; j++) {
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition position = new ChessPosition(2, j);
            squares[position.getRow()][position.getColumn()] = piece;
        }
        for (int j = 1; j <= 8; j++) {
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPosition position = new ChessPosition(7, j);
            squares[position.getRow()][position.getColumn()] = piece;
        }

        for (int k = 1; k <= 2; k++) {
            int i;
            ChessGame.TeamColor color;
            if (k == 1){
                i = 1;
                color = ChessGame.TeamColor.WHITE;
            }
            else {
                i = 8;
                color = ChessGame.TeamColor.BLACK;
            }

            for (int j = 1; j <= 8; j++) {
                if (j == 1 || j == 8) {
                    ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.ROOK);
                    ChessPosition position = new ChessPosition(i, j);
                    squares[position.getRow()][position.getColumn()] = piece;
                }
                if (j == 2 || j == 7) {
                    ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
                    ChessPosition position = new ChessPosition(i, j);
                    squares[position.getRow()][position.getColumn()] = piece;
                }
                if (j == 3 || j == 6) {
                    ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
                    ChessPosition position = new ChessPosition(i, j);
                    squares[position.getRow()][position.getColumn()] = piece;
                }
                if (j == 4) {
                    ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
                    ChessPosition position = new ChessPosition(i, j);
                    squares[position.getRow()][position.getColumn()] = piece;
                }
                if (j == 5) {
                    ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.KING);
                    ChessPosition position = new ChessPosition(i, j);
                    squares[position.getRow()][position.getColumn()] = piece;
                }
            }
        }
    }
}
