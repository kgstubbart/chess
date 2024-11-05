package dataaccess;

import model.UserData;
import service.ServiceException;

import java.sql.*;

public class MySqlUserDataAccess implements UserDataAccess {
    public MySqlUserDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `auth` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(auth)
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

    public UserData getUser(String userName) {
        return null;
    }

    public void createUser(UserData userData) throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new ServiceException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public void clear() {

    }
}