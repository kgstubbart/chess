package chess;

import chess.movement.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public ChessPiece clone() {
        return new ChessPiece(this.pieceColor, this.type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (board.getPiece(myPosition).type) {
            case KING:
                KingMovement kingMovement = new KingMovement(board, myPosition);
                return kingMovement.moves(board, myPosition);
            case QUEEN:
                QueenMovement queenMovement = new QueenMovement(board, myPosition);
                return queenMovement.moves(board, myPosition);
            case ROOK:
                RookMovement rookMovement = new RookMovement(board, myPosition);
                return rookMovement.moves(board, myPosition);
            case BISHOP:
                BishopMovement bishopMovement = new BishopMovement(board, myPosition);
                return bishopMovement.moves(board, myPosition);
            case KNIGHT:
                KnightMovement knightMovement = new KnightMovement(board, myPosition);
                return knightMovement.moves(board, myPosition);
            case PAWN:
                PawnMovement pawnMovement = new PawnMovement(board, myPosition);
                return pawnMovement.moves(board, myPosition);
        }
        return null;
    }
}
