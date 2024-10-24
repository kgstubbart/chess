package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import model.GameData;
import model.JoinGameData;

import java.util.Map;

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
        return gameDataAccess.createGame(nameOfGame);
    }

    public GameData joinGame(String authToken, JoinGameData joinGameData) throws ServiceException {
        ChessGame.TeamColor playerColor = joinGameData.playerColor();
        int gameID = joinGameData.gameID();
        GameData gameData = gameDataAccess.getGame(gameID);

        if ((playerColor == null) || (gameData == null)) {
            throw new ServiceException("Error: bad request");
        }
        if (authDataAccess.getAuth(authToken) == null) {
            throw new ServiceException("Error: unauthorized");
        }

        GameData game = gameDataAccess.updateGame(authDataAccess.getAuth(authToken).username(), playerColor, gameID, gameData);

        if (game == null) {
            throw new ServiceException("Error: already taken");
        }

        return game;
    }

    public void clearGames() throws ServiceException {
        gameDataAccess.clear();
        authDataAccess.clear();
    }
}
