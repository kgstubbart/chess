package dataaccess;

import model.GameData;

public interface GameDataAccess {
    GameData createGame(String authToken, String gameName);
}
