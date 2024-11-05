package dataaccess;

import model.AuthData;
import model.UserData;
import service.ServiceException;

import java.sql.*;
import java.util.UUID;

public class MySqlAuthDataAccess implements AuthDataAccess {
    public MySqlAuthDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken),
              INDEX(username)
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

    public String createAuth(UserData userData) throws ServiceException {
        String authToken;
        try (var conn = DatabaseManager.getConnection()) {
            authToken = UUID.randomUUID().toString();
            String username = userData.username();
            try (var preparedStatement = conn.prepareStatement("INSERT INTO AuthData (authToken, username) VALUES (?, ?)")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to create auth: %s", e.getMessage()));
        }
        return authToken;
    }

    public AuthData getAuth(String authToken) throws ServiceException {
        AuthData authData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM AuthData WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        authData = new AuthData(authToken, username);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to find user: %s", e.getMessage()));
        }
        return authData;
    }

    public void deleteAuth(AuthData authData) throws ServiceException {
        String authToken = authData.authToken();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM AuthData WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to delete Auth: %s", e.getMessage()));
        }
    }

    public void clear() throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM AuthData")) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to clear Auth Database: %s", e.getMessage()));
        }
    }
}