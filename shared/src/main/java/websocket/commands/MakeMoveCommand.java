package websocket.commands;

import chess.ChessMove;
import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand {
    private final String username;
    private final ChessMove move;
    public MakeMoveCommand(CommandType commandType, String authToken, String username, Integer gameID,
                           ChessMove move) {
        super(commandType, authToken, gameID);
        this.username = username;
        this.move = move;
    }

    public String getUsername() {
        return username;
    }

    public ChessMove getMove() {
        return move;
    }
}
