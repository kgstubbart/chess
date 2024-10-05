package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        this.board.resetBoard();
        this.team = getTeamTurn();
    }

    public ChessGame(ChessGame copy) {
        this.num_moves = copy.num_moves;
        this.team = copy.team;
        this.startPosition = copy.startPosition;
        this.board = ChessBoard.copyOf(copy.board);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return num_moves == chessGame.num_moves && team == chessGame.team && Objects.equals(startPosition, chessGame.startPosition) && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num_moves, team, startPosition, board);
    }

    @Override
    public String
    toString() {
        return "ChessGame{" +
                "num_moves=" + num_moves +
                ", team=" + team +
                ", startPosition=" + startPosition +
                ", board=" + board +
                '}';
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
        if (team == TeamColor.BLACK) {
            num_moves++;
        }
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
        ChessPiece chessPiece = board.getPiece(startPosition);
        if (chessPiece == null) {
            return null;
        }
        ChessGame.TeamColor teamColor = chessPiece.getTeamColor();
        Collection<ChessMove> moves = chessPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        for (ChessMove move : moves) {
            ChessBoard board_copy = ChessBoard.copyOf(board);
            board_copy.addPiece(startPosition, null);
            board_copy.addPiece(move.getEndPosition(), chessPiece);

            ChessPosition king_position = findKing(board_copy, teamColor);
            if (king_position != null) {
                KingDanger kingDanger = new KingDanger(board_copy, king_position, teamColor);
                if (!kingDanger.BaseDanger(board_copy, king_position, teamColor)){
                    validMoves.add(move);
                }
            }
            else {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Invalid move: no piece at " + startPosition);
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid move - not your turn");
        }
        if (!validMoves(startPosition).contains(move)) {
            throw new InvalidMoveException("Invalid move: " + startPosition + " - " + move.getEndPosition());
        }
        ChessPiece chessPiece = board.getPiece(startPosition);
        board.addPiece(startPosition, null);
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(chessPiece.getTeamColor(), move.getPromotionPiece()));
        }
        else {
            board.addPiece(move.getEndPosition(), chessPiece);
        }
        this.num_moves++;
    }

    /** @return position of king on the board
     *
     */

    public ChessPosition findKing(ChessBoard board, TeamColor teamColor) {
        for (int row = 1; row <= 8; row ++) {
            for (int col = 1; col <= 8; col++) {
                if (board.getPiece(new ChessPosition(row, col)) != null) {
                   ChessPiece piece = board.getPiece(new ChessPosition(row, col));
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
        ChessPosition king_position = findKing(board, teamColor);
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
        if (isInCheck(teamColor)) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    if (board.getPiece(new ChessPosition(row, col)) != null) {
                        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                        ChessPosition pos = new ChessPosition(row, col);
                        if (piece.getTeamColor() == teamColor) {
                            if (!validMoves(pos).isEmpty()) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    if (board.getPiece(new ChessPosition(row, col)) != null) {
                        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                        ChessPosition pos = new ChessPosition(row, col);
                        if (piece.getTeamColor() == teamColor) {
                            if (!validMoves(pos).isEmpty()) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        if (board == null) {
            this.board = new ChessBoard();
            this.board.resetBoard();
        }
        else {
            this.board = board;
        }
        this.num_moves = 0;
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
