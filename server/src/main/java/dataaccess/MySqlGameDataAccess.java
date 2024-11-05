package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.ServiceException;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  GameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(game)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public GameData createGame(String gameName) throws ServiceException {
        GameData gameData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO GameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)")) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, new Gson().toJson(new ChessGame()));
                preparedStatement.executeUpdate();
                try (var newPreparedStatement = conn.prepareStatement("SELECT gameID FROM GameData WHERE gameName = ?")) {
                    newPreparedStatement.setString(1, gameName);
                    try (var rs = newPreparedStatement.executeQuery()) {
                        if (rs.next()) {
                            int gameID = rs.getInt("gameID");
                            gameData = new GameData(gameID, null, null, gameName, new ChessGame());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to create game: %s", e.getMessage()));
        }
        return gameData;
    }

    public GameData getGame(int gameID) throws ServiceException {
        GameData gameData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, game FROM GameData WHERE gameID = ?")) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, new Gson().fromJson(game, ChessGame.class));
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to get game: %s", e.getMessage()));
        }
        return gameData;
    }

    public GameData updateGame(String username, ChessGame.TeamColor playerColor, int gameID, GameData gameData) {
        return null;
    }

    public Collection<GameData> listGames() {
        return List.of();
    }

    public void clear() throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM GameData")) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to clear Game Database: %s", e.getMessage()));
        }
    }
}