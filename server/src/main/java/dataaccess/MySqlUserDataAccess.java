package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.ServiceException;

import java.sql.*;

public class MySqlUserDataAccess implements UserDataAccess {
    public MySqlUserDataAccess() {
        try {
            configureUserDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureUserDatabase() throws DataAccessException {
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

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private String readHashedPasswordFromDatabase(String username) throws ServiceException {
        String hashedPassword = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT password FROM UserData WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        hashedPassword = rs.getString("password");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to find password for user: %s", e.getMessage()));
        }
        return hashedPassword;
    }

    public UserData getUser(String username, String password) throws ServiceException {
        UserData userData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT password, email FROM UserData WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        String email = rs.getString("email");
                        if (BCrypt.checkpw(password, hashedPassword)) {
                            userData = new UserData(username, password, email);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to find user: %s", e.getMessage()));
        }
        return userData;
    }

    public void createUser(UserData userData) throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {
            String username = userData.username();
            if (readHashedPasswordFromDatabase(username) != null) {
                throw new ServiceException(String.format("User %s already exists", username));
            }
            String password = userData.password();
            String hashedPassword = hashUserPassword(password);
            String email = userData.email();
            try (var preparedStatement = conn.prepareStatement("INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to register user: %s", e.getMessage()));
        }
    }

    public void clear() throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM UserData")) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to clear User Database: %s", e.getMessage()));
        }
    }
}