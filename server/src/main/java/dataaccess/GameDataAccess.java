package dataaccess;

import chess.ChessGame;
import model.GameData;
import service.ServiceException;

import java.util.Collection;

public interface GameDataAccess {
    GameData createGame(String gameName) throws ServiceException;

    GameData getGame(int gameID) throws ServiceException;

    GameData updateGame(String username, ChessGame.TeamColor playerColor, int gameID, GameData gameData) throws ServiceException;

    Collection<GameData> listGames() throws ServiceException;

    void clear() throws ServiceException;
}
