package service;

import dataaccess.GameDataAccess;
import model.GameData;

public class GameService {
    private final GameDataAccess gameDataAccess;

    public GameService(GameDataAccess gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    public GameData createGame(String authToken, GameData gameName) {
        String nameOfGame = gameName.gameName();
        return gameDataAccess.createGame(authToken, nameOfGame);
    }
}
