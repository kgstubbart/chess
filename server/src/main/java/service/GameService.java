package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import model.GameData;

public class GameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public GameData createGame(String authToken, GameData gameName) throws ServiceException {
        String nameOfGame = gameName.gameName();
        if (authDataAccess.getAuth(authToken) == null) {
            throw new ServiceException("Error: unauthorized");
        }
        return gameDataAccess.createGame(authToken, nameOfGame);
    }
}
