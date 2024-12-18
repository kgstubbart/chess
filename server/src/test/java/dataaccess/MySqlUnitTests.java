package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.ServiceException;
import service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MySqlUnitTests {
    static final MySqlAuthDataAccess AUTH_DATA_ACCESS = new MySqlAuthDataAccess();
    static final UserService USER_SERVICE = new UserService(new MySqlUserDataAccess(), AUTH_DATA_ACCESS);
    static final GameService GAME_SERVICE = new GameService(new MySqlGameDataAccess(), AUTH_DATA_ACCESS);

    @BeforeEach
    void clear() throws ServiceException {
        USER_SERVICE.clearUsers();
        GAME_SERVICE.clearGames();
    }

    @Test
    void positiveMySqlUserDataAccess() {
        MySqlUserDataAccess userDataAccess = new MySqlUserDataAccess();
        assertNotNull(userDataAccess);
    }

    @Test
    void positiveMySqlAuthDataAccess() {
        MySqlAuthDataAccess authDataAccess = new MySqlAuthDataAccess();
        assertNotNull(authDataAccess);
    }

    @Test
    void positiveMySqlGameDataAccess() {
        MySqlGameDataAccess gameDataAccess = new MySqlGameDataAccess();
        assertNotNull(gameDataAccess);
    }

    @Test
    void positiveRegisterUser() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        AuthData authData = USER_SERVICE.registerUser(userData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negativeRegisterUser() {
        UserData userData = new UserData("playstation", null, "xbox@mail");

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> USER_SERVICE.registerUser(userData),
                "Expected ServiceException due to invalid user data."
        );
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void positiveLoginUser() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("mistborn", "stormlight", null);
        AuthData authData = USER_SERVICE.loginUser(userLoginData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negativeLoginUser() throws ServiceException {
        UserData userData = new UserData("playstation", "switch", "xbox@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("playstation", "pc", null);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> USER_SERVICE.loginUser(userLoginData),
                "Expected ServiceException due to invalid user data."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void positiveLogoutUser() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("mistborn", "stormlight", null);
        AuthData authData = USER_SERVICE.loginUser(userLoginData);
        String authToken = authData.authToken();
        USER_SERVICE.logoutUser(authToken);

        assertNull(USER_SERVICE.authDataAccess.getAuth(authToken));
    }

    @Test
    void negativeLogoutUser() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> USER_SERVICE.logoutUser("fake_token"),
                "Expected ServiceException due to invalid auth token."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void positiveCreateGame() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("mistborn", "stormlight", null);
        AuthData authData = USER_SERVICE.loginUser(userLoginData);
        String authToken = authData.authToken();
        GameData gameName = new GameData(0, null, null, "Good Create Game", null);
        GameData game = GAME_SERVICE.createGame(authToken, gameName);

        assertTrue(game.gameID() > 0);
    }

    @Test
    void negativeCreateGame() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> GAME_SERVICE.createGame("fake_token", new GameData(0, null, null, "Bad Create Game", null)),
                "Expected ServiceException due to invalid auth token."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void positiveJoinGame() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("mistborn", "stormlight", null);
        AuthData authData = USER_SERVICE.loginUser(userLoginData);
        String authToken = authData.authToken();
        GameData gameName = new GameData(0, null, null, "Good Join Game", null);
        GameData gameData = GAME_SERVICE.createGame(authToken, gameName);
        GameData joinedGame = GAME_SERVICE.joinGame(authToken, new JoinGameData(ChessGame.TeamColor.BLACK, gameData.gameID()));

        assertEquals(joinedGame.blackUsername(), "mistborn");
    }

    @Test
    void negativeJoinGame() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> GAME_SERVICE.joinGame("fake_token", new JoinGameData(ChessGame.TeamColor.WHITE, 9876)),
                "Expected ServiceException due to invalid auth token."
        );
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void positiveListGames() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("mistborn", "stormlight", null);
        AuthData authData = USER_SERVICE.loginUser(userLoginData);
        String authToken = authData.authToken();

        Map<String, GameData> expectedGames = new HashMap<>();
        expectedGames.put("1", GAME_SERVICE.createGame(authToken, new GameData(0, null, null, "First List Game", null)));
        expectedGames.put("2", GAME_SERVICE.createGame(authToken, new GameData(0, null, null, "Second List Game", null)));
        expectedGames.put("3", GAME_SERVICE.createGame(authToken, new GameData(0, null, null, "Third List Game", null)));

        var actual = GAME_SERVICE.listGames(authToken);
        assertIterableEquals(expectedGames.values(), actual);
    }

    @Test
    void negativeListGames() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> GAME_SERVICE.listGames("fake_token"),
                "Expected ServiceException due to invalid auth token."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clearUsersGamesAuths() throws ServiceException {
        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
        USER_SERVICE.registerUser(userData);
        UserData userLoginData = new UserData("mistborn", "stormlight", null);
        AuthData authData = USER_SERVICE.loginUser(userLoginData);
        String authToken = authData.authToken();
        GAME_SERVICE.clearGames();
        Collection<GameData> games = GAME_SERVICE.listGames(authToken);
        assertTrue(games.isEmpty());

        USER_SERVICE.clearUsers();
        assertNull(USER_SERVICE.userDataAccess.getUser(userData.username(), userData.password()));
        assertNull(USER_SERVICE.authDataAccess.getAuth(authToken));
    }
}
