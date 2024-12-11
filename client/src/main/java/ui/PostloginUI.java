package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.ChessGame;
import model.*;
import ui.facade.*;

public class PostloginUI {
    private final ServerFacade server;
    private final WebSocketFacade webSocket;
    private String authToken;
    private boolean inGame = false;
    private Integer gameID;
    private String username;

    public PostloginUI(String serverUrl, String authToken, String username, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        webSocket = new WebSocketFacade(serverUrl, notificationHandler);
        this.authToken = authToken;
        this.username = username;
    }

    public String eval(String input) throws FacadeException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "create" -> create(params);
            case "join" -> join(params);
            case "observe" -> observe(params);
            case "list" -> list(params);
            case "logout" -> logout(params);
            default -> help();
        };
    }

    public String create(String... params) throws FacadeException {
        if (params.length != 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Expected: <NAME>" + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            var gameName = params[0];
            GameData gameData = new GameData(0, null, null, gameName, null);
            server.createGame(gameData, authToken);
            return "Created game: " + gameName + "\n";
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String join(String... params) throws FacadeException {
        if (params.length != 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Expected: <ID> <WHITE/BLACK>" + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            var gameNumber = params[0];
            var stringPlayerColor = params[1].toLowerCase();
            ChessGame.TeamColor playerColor;
            if (stringPlayerColor.equals("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            }
            else if (stringPlayerColor.equals("black")) {
                playerColor = ChessGame.TeamColor.BLACK;
            }
            else {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Chose either white or black." + EscapeSequences.RESET_TEXT_COLOR + "\n";
            }

            GameData[] allGames = server.listGames(authToken);
            if (allGames.length == 0) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "No games currently available." + EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            if ((Integer.parseInt(gameNumber) <= 0) || (Integer.parseInt(gameNumber) > allGames.length)) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Game ID does not exist." + EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            GameData game = allGames[Integer.parseInt(gameNumber) - 1];
            gameID = game.gameID();

            JoinGameData joinGameData = new JoinGameData(playerColor, gameID);
            server.joinGame(joinGameData, authToken);
            webSocket.enterGameplay(authToken, username, game.gameID());
            inGame = true;
            // print board
            return "Joined game number: " + gameNumber + "\n";
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String observe(String... params) throws FacadeException {
        if (params.length != 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Expected: <ID>" + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            var gameNumber = params[0];

            GameData[] allGames = server.listGames(authToken);
            if (allGames.length == 0) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "No games currently available." + EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            if ((Integer.parseInt(gameNumber) <= 0) || (Integer.parseInt(gameNumber) > allGames.length)) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Game number does not exist." + EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            GameData game = allGames[Integer.parseInt(gameNumber) - 1];

            JoinGameData joinGameData = new JoinGameData(ChessGame.TeamColor.OBSERVER, game.gameID());
            server.joinGame(joinGameData, authToken);
            webSocket.enterGameplay(authToken, username, game.gameID());
            inGame = true;
            // print board
            return "Observing game number: " + gameNumber + "\n";
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String list(String... params) throws FacadeException {
        if (params.length != 0) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "List needs no additional information." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            GameData[] allGames = server.listGames(authToken);
            if (allGames.length == 0) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "No games currently available." + EscapeSequences.RESET_TEXT_COLOR + "\n";
            }

            List<String> gameList = getGameStrings(allGames);
            return String.join("\n" ,gameList) + "\n";
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String logout(String... params) throws FacadeException {
        if (params.length != 0) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Logout needs no additional information." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            server.logoutUser(authToken);
            authToken = null;
            return """
                    Successfully logged out.
                    
                    """;
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String help() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "    create <NAME>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - create a new game" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    join <ID> <WHITE/BLACK>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - join a game" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    observe <ID>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - watch a game" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    list" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - see a list of all games" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    logout" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - log out of current account" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    help" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - see all available commands" + EscapeSequences.RESET_TEXT_COLOR + "\n";
    }

    private List<String> getGameStrings(GameData[] allGames) {
        List<String> gameList = new ArrayList<>();
        for (int i = 0; i < allGames.length; i++) {
            GameData gameData = allGames[i];
            String whiteUsername = gameData.whiteUsername();
            whiteUsername = formatNullUsername(whiteUsername);
            String blackUsername = gameData.blackUsername();
            blackUsername = formatNullUsername(blackUsername);

            gameList.add(String.format("%d | Game: %s | White Player: %s | Black Player: %s",
                    i+1, gameData.gameName(), whiteUsername, blackUsername));
        }
        return gameList;
    }

    private String formatNullUsername(String username) {
        if (username == null) {
            return "None";
        }
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public boolean getInGame() {
        return inGame;
    }

    public Integer getGameID() {
        return gameID;
    }
}
