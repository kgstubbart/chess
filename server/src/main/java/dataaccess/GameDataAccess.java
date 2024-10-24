package dataaccess;

import chess.ChessGame;
import model.GameData;

public interface GameDataAccess {
    GameData createGame(String gameName);

    GameData getGame(int gameID);

    GameData updateGame(String username, ChessGame.TeamColor playerColor, int gameID, GameData gameData);

    void clear();
}
