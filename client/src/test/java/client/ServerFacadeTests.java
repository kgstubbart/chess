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

import java.util.*;

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
        assertEquals("400: Error: bad register request", exception.getMessage());
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
        assertEquals("400: Error: bad login request", exception.getMessage());
    }

    @Test
    void positiveLogoutUser() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);
        String authToken = authData.authToken();
        facade.logoutUser(authToken);
        AuthData newAuthData = facade.loginUser(userLoginData);

        assertNotEquals(authToken, newAuthData.authToken());
    }

    @Test
    void negativeLogoutUser() {
        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.logoutUser("not_real_token"),
                "Expected FacadeException due to invalid auth token."
        );
        assertEquals("400: Error: bad logout request", exception.getMessage());
    }

    @Test
    void positiveCreateGame() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);
        String authToken = authData.authToken();
        GameData gameName = new GameData(0, null, null, "Valid Create Game", null);
        GameData game = facade.createGame(gameName, authToken);

        assertTrue(game.gameID() > 0);
    }

    @Test
    void negativeCreateGame() {
        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.createGame(new GameData(0, null, null,
                        "Invalid Create Game", null), "not_real_token"),
                "Expected FacadeException due to invalid auth token."
        );
        assertEquals("400: Error: bad create request", exception.getMessage());
    }

    @Test
    void positiveJoinGame() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);
        String authToken = authData.authToken();
        GameData gameName = new GameData(0, null, null, "Valid Join Game", null);
        GameData gameData = facade.createGame(gameName, authToken);
        GameData joinedGame = facade.joinGame(new JoinGameData(ChessGame.TeamColor.BLACK, gameData.gameID()), authToken);

        assertEquals(joinedGame.blackUsername(), "captain");
    }

    @Test
    void negativeJoinGame() {
        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.joinGame(new JoinGameData(ChessGame.TeamColor.WHITE, 9876), "not_real_token"),
                "Expected FacadeException due to invalid auth token."
        );
        assertEquals("400: Error: bad join request", exception.getMessage());
    }

    @Test
    void positiveListGames() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);
        String authToken = authData.authToken();

        Map<String, GameData> expectedGames = new HashMap<>();
        expectedGames.put("1", facade.createGame(new GameData(0, null, null, "First List Game", null), authToken));
        expectedGames.put("2", facade.createGame(new GameData(0, null, null, "Second List Game", null), authToken));
        expectedGames.put("3", facade.createGame(new GameData(0, null, null, "Third List Game", null), authToken));

        var actual = facade.listGames(authToken);
        assertIterableEquals(expectedGames.values(), Arrays.asList(actual));
    }

    @Test
    void negativeListGames() {
        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.listGames("not_real_token"),
                "Expected FacadeException due to invalid auth token."
        );
        assertEquals("400: Error: bad list request", exception.getMessage());
    }

    @Test
    void positiveClearApplication() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);
        String authToken = authData.authToken();
        facade.createGame(new GameData(0, null, null, "Positive Clear Game", null), authToken);
        facade.clearApplication("mandostormsslug");
        facade.registerUser(userData);
        authData = facade.loginUser(userLoginData);
        authToken = authData.authToken();
        Collection<GameData> games = List.of(facade.listGames(authToken));
        assertTrue(games.isEmpty());
    }

    @Test
    void negativeClearApplication() throws FacadeException {
        UserData userData = new UserData("captain", "america", "shield@mail");
        facade.registerUser(userData);
        UserData userLoginData = new UserData("captain", "america", null);
        AuthData authData = facade.loginUser(userLoginData);
        String authToken = authData.authToken();
        facade.createGame(new GameData(0, null, null, "Positive Clear Game", null), authToken);
        FacadeException exception = assertThrows(
                FacadeException.class,
                () -> facade.clearApplication("wrong_admin_password"),
                "Expected FacadeException due to invalid auth token."
        );
        assertEquals("400: Error: bad clear request", exception.getMessage());
    }
}
