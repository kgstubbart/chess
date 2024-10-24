package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    GameData createGame(String gameName);

    GameData getGame(int gameID);

    GameData updateGame(String username, ChessGame.TeamColor playerColor, int gameID, GameData gameData);

    Collection<GameData> listGames();

    void clear();
}
