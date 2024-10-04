package chess;

public class KingDanger {
    private final ChessBoard board;
    private final ChessPosition pos;
    private final ChessGame.TeamColor teamColor;

    public KingDanger(ChessBoard board, ChessPosition pos, ChessGame.TeamColor teamColor) {
        this.board = board;
        this.pos = pos;
        this.teamColor = teamColor;
    }

    protected boolean BaseDanger(ChessBoard board, ChessPosition pos, ChessGame.TeamColor teamColor) {
        // check for king (may be able to do pawn here too)

        // check for pawn

        // check for knight

        // check diagonal

        // check straight
        return false;
    }
}
