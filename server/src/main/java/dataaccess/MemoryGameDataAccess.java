package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDataAccess implements GameDataAccess {
    private int gameID = 0;
    final private Map<String, GameData> games = new HashMap<>();

    @Override
    public GameData createGame(String gameName) {
        gameID++;
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        String strGameID = Integer.toString(gameID);
        games.put(strGameID, game);
        return game;
    }

    @Override
    public GameData getGame(int gameID) {
        String strGameID = Integer.toString(gameID);
        return games.get(strGameID);
    }

    @Override
    public GameData updateGame(String username, ChessGame.TeamColor playerColor, int gameID, GameData gameData) {
        String strGameID = Integer.toString(gameID);
        GameData game;
        if (Objects.equals(playerColor, ChessGame.TeamColor.WHITE)) {
            if (gameData.whiteUsername() != null) {
                return null;
            }
            game = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else {
            if (gameData.blackUsername() != null) {
                return null;
            }
            game = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        }
        games.put(strGameID, game);
        return game;
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void clear() {
        games.clear();
        gameID = 0;
    }
}
