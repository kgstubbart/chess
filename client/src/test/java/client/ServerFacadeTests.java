package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ServiceException;
import ui.facade.FacadeException;
import ui.facade.ServerFacade;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    void clear() throws FacadeException {
        facade.clearApplication("mandostormsslug");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void positiveRegister() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        AuthData authData = facade.registerUser(userData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negativeRegisterUser() {
        UserData userData = new UserData("iron", null, "suit@mail");

        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.registerUser(userData),
                "Expected FacadeException due to invalid user data."
        );
        assertEquals("400: Error: bad request", exception.getMessage());
    }

    @Test
    void positiveLoginUser() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negativeLoginUser() throws FacadeException {
        UserData userData = new UserData("iron", "man", "suit@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("iron", "patriot", null);

        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.loginUser(userLoginData),
                "Expected FacadeException due to invalid user data."
        );
        assertEquals("400: Error: bad request", exception.getMessage());
    }

//    @Test
//    void positiveLogoutUser() throws FacadeException {
//        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
//        USER_SERVICE.registerUser(userData);
//        UserData userLoginData = new UserData("mistborn", "stormlight", null);
//        AuthData authData = USER_SERVICE.loginUser(userLoginData);
//        String authToken = authData.authToken();
//        USER_SERVICE.logoutUser(authToken);
//
//        assertNull(USER_SERVICE.authDataAccess.getAuth(authToken));
//    }
//
//    @Test
//    void negativeLogoutUser() {
//        ServiceException exception = assertThrows(
//                ServiceException.class,
//                () -> USER_SERVICE.logoutUser("fake_token"),
//                "Expected ServiceException due to invalid auth token."
//        );
//        assertEquals("Error: unauthorized", exception.getMessage());
//    }
//
//    @Test
//    void positiveCreateGame() throws FacadeException {
//        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
//        USER_SERVICE.registerUser(userData);
//        UserData userLoginData = new UserData("mistborn", "stormlight", null);
//        AuthData authData = USER_SERVICE.loginUser(userLoginData);
//        String authToken = authData.authToken();
//        GameData gameName = new GameData(0, null, null, "Good Create Game", null);
//        GameData game = GAME_SERVICE.createGame(authToken, gameName);
//
//        assertTrue(game.gameID() > 0);
//    }
//
//    @Test
//    void negativeCreateGame() {
//        ServiceException exception = assertThrows(
//                ServiceException.class,
//                () -> GAME_SERVICE.createGame("fake_token", new GameData(0, null, null, "Bad Create Game", null)),
//                "Expected ServiceException due to invalid auth token."
//        );
//        assertEquals("Error: unauthorized", exception.getMessage());
//    }
//
//    @Test
//    void positiveJoinGame() throws FacadeException {
//        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
//        USER_SERVICE.registerUser(userData);
//        UserData userLoginData = new UserData("mistborn", "stormlight", null);
//        AuthData authData = USER_SERVICE.loginUser(userLoginData);
//        String authToken = authData.authToken();
//        GameData gameName = new GameData(0, null, null, "Good Join Game", null);
//        GameData gameData = GAME_SERVICE.createGame(authToken, gameName);
//        GameData joinedGame = GAME_SERVICE.joinGame(authToken, new JoinGameData(ChessGame.TeamColor.BLACK, gameData.gameID()));
//
//        assertEquals(joinedGame.blackUsername(), "mistborn");
//    }
//
//    @Test
//    void negativeJoinGame() {
//        ServiceException exception = assertThrows(
//                ServiceException.class,
//                () -> GAME_SERVICE.joinGame("fake_token", new JoinGameData(ChessGame.TeamColor.WHITE, 9876)),
//                "Expected ServiceException due to invalid auth token."
//        );
//        assertEquals("Error: bad request", exception.getMessage());
//    }
//
//    @Test
//    void positiveListGames() throws FacadeException {
//        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
//        USER_SERVICE.registerUser(userData);
//        UserData userLoginData = new UserData("mistborn", "stormlight", null);
//        AuthData authData = USER_SERVICE.loginUser(userLoginData);
//        String authToken = authData.authToken();
//
//        Map<String, GameData> expectedGames = new HashMap<>();
//        expectedGames.put("1", GAME_SERVICE.createGame(authToken, new GameData(0, null, null, "First List Game", null)));
//        expectedGames.put("2", GAME_SERVICE.createGame(authToken, new GameData(0, null, null, "Second List Game", null)));
//        expectedGames.put("3", GAME_SERVICE.createGame(authToken, new GameData(0, null, null, "Third List Game", null)));
//
//        var actual = GAME_SERVICE.listGames(authToken);
//        assertIterableEquals(expectedGames.values(), actual);
//    }
//
//    @Test
//    void negativeListGames() {
//        ServiceException exception = assertThrows(
//                ServiceException.class,
//                () -> GAME_SERVICE.listGames("fake_token"),
//                "Expected ServiceException due to invalid auth token."
//        );
//        assertEquals("Error: unauthorized", exception.getMessage());
//    }
//
//    @Test
//    void clearUsersGamesAuths() throws FacadeException {
//        UserData userData = new UserData("mistborn", "stormlight", "sunlight@mail");
//        USER_SERVICE.registerUser(userData);
//        UserData userLoginData = new UserData("mistborn", "stormlight", null);
//        AuthData authData = USER_SERVICE.loginUser(userLoginData);
//        String authToken = authData.authToken();
//        GAME_SERVICE.clearGames();
//        Collection<GameData> games = GAME_SERVICE.listGames(authToken);
//        assertTrue(games.isEmpty());
//
//        USER_SERVICE.clearUsers();
//        assertNull(USER_SERVICE.userDataAccess.getUser(userData.username(), userData.password()));
//        assertNull(USER_SERVICE.authDataAccess.getAuth(authToken));
//    }
}
