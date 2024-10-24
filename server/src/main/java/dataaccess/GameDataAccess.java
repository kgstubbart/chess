package dataaccess;

import model.GameData;

public interface GameDataAccess {
    GameData createGame(String gameName);

    GameData getGame(int gameID);

    GameData updateGame(String username, String playerColor, int gameID, GameData gameData);

    void clear();
}
