package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceUnitTests {
    static final MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    static final UserService userService = new UserService(new MemoryUserDataAccess(), authDataAccess);
    static final GameService gameService = new GameService(new MemoryGameDataAccess(), authDataAccess);

    @BeforeEach
    void clear() throws ServiceException {
        userService.clearUsers();
    }

    @Test
    void positive_registerUser() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        AuthData authData = userService.registerUser(userData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negative_registerUser() throws ServiceException {
        UserData userData = new UserData("cheese", null, "crackers@mail");

        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> userService.registerUser(userData),
            "Expected ServiceException due to invalid user data."
        );
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void positive_loginUser() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        userService.registerUser(userData);
        UserData userLoginData = new UserData("apple", "banana", null);
        AuthData authData = userService.loginUser(userLoginData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negative_loginUser() throws ServiceException {
        UserData userData = new UserData("cheese", "bread", "crackers@mail");
        userService.registerUser(userData);
        UserData userLoginData = new UserData("cheese", "banana", null);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> userService.loginUser(userLoginData),
                "Expected ServiceException due to invalid user data."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void positive_logoutUser() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        userService.registerUser(userData);
        UserData userLoginData = new UserData("apple", "banana", null);
        AuthData authData = userService.loginUser(userLoginData);
        String authToken = authData.authToken();
        userService.logoutUser(authToken);

        assertNull(userService.authDataAccess.getAuth(authToken));
    }

    @Test
    void negative_logoutUser() throws ServiceException {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> userService.logoutUser("bad_token"),
                "Expected ServiceException due to invalid auth token."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void positive_createGame() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        userService.registerUser(userData);
        UserData userLoginData = new UserData("apple", "banana", null);
        AuthData authData = userService.loginUser(userLoginData);
        String authToken = authData.authToken();
        GameData gameName = new GameData(0, null, null, "Best Game Ever", null);
        GameData game = gameService.createGame(authToken, gameName);

        assertTrue(game.gameID() > 0);
    }

    @Test
    void negative_createGame() {
    }

    @Test
    void positive_joinGame() {
    }

    @Test
    void negative_joinGame() {
    }

    @Test
    void positive_listGames() {
    }

    @Test
    void negative_listGames() {
    }

    @Test
    void clearUsers() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        userService.registerUser(userData);
        UserData userLoginData = new UserData("apple", "banana", null);
        AuthData authData = userService.loginUser(userLoginData);
        String authToken = authData.authToken();
        userService.clearUsers();

        assertNull(userService.userDataAccess.getUser(userData.username()));
        assertNull(userService.authDataAccess.getAuth(authToken));
    }
}
