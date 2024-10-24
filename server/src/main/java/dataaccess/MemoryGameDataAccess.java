package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

public class MemoryGameDataAccess implements GameDataAccess {
    private int gameID = 0;

    @Override
    public GameData createGame(String authToken, String gameName) {;
        gameID++;
        return new GameData(gameID, null, null, gameName, new ChessGame());
    }
}
