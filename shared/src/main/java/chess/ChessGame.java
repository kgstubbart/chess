package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private int num_moves = 0;
    private TeamColor team;
    private ChessPosition startPosition;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {

    }

    public ChessGame(ChessGame copy) {
        this.num_moves = copy.num_moves;
        this.team = copy.team;
        this.startPosition = copy.startPosition;
        this.board = ChessBoard.copyOf(copy.board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (this.num_moves % 2 == 0) {
            return TeamColor.WHITE;
        }
        else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        this.startPosition = startPosition;
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        this.num_moves++;
    }

    /** @return position of king on the board
     *
     */

    public ChessPosition findKing(TeamColor teamColor) {
        for (int row = 0; row <= 7; row ++) {
            for (int col = 0; col <= 7; col++) {
                if (getBoard().getPiece(new ChessPosition(row, col)) != null) {
                   ChessPiece piece = getBoard().getPiece(new ChessPosition(row, col));
                   if ((piece.getTeamColor() == teamColor) && (piece.getPieceType() == ChessPiece.PieceType.KING)) {
                       return new ChessPosition(row, col);
                   }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_position = findKing(teamColor);
        if (king_position != null) {
            KingDanger kingDanger = new KingDanger(board, king_position, teamColor);
            return kingDanger.BaseDanger(board, king_position, teamColor);
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
