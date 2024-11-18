package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.ChessGame;
import model.*;
import ui.facade.*;

public class PostloginUI {
    private final ServerFacade server;
    private final String authToken;

    public PostloginUI(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
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
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String create(String... params) throws FacadeException {
        if (params.length != 1) {
            throw new FacadeException(400, "Create needs a game name.");
        }
        try {
            var gameName = params[0];
            GameData gameData = new GameData(0, null, null, gameName, null);
            server.createGame(gameData, authToken);
            return "Created game: " + gameName;
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String join(String... params) throws FacadeException {
        if (params.length != 2) {
            throw new FacadeException(400, "Create needs a game number and player color.");
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
                throw new FacadeException(400, "Chose either white or black");
            }

            GameData[] allGames = server.listGames(authToken);
            if (allGames.length == 0) {
                return "No games currently available.";
            }
            GameData game = allGames[Integer.parseInt(gameNumber) - 1];

            JoinGameData joinGameData = new JoinGameData(playerColor, game.gameID());
            server.joinGame(joinGameData, authToken);
            return "Joined game number: " + gameNumber;
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String observe(String... params) throws FacadeException {
        if (params.length != 1) {
            throw new FacadeException(400, "Create needs a game number.");
        }
        try {
            var gameNumber = params[0];

            GameData[] allGames = server.listGames(authToken);
            if (allGames.length == 0) {
                return "No games currently available.";
            }
            GameData game = allGames[Integer.parseInt(gameNumber) - 1];

            JoinGameData joinGameData = new JoinGameData(null, game.gameID());
            server.joinGame(joinGameData, authToken);
            return "Observing game number: " + gameNumber;
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String list(String... params) throws FacadeException {
        if (params.length != 0) {
            throw new FacadeException(400, "List needs no additional information.");
        }
        try {
            GameData[] allGames = server.listGames(authToken);
            if (allGames.length == 0) {
                return "No games currently available.";
            }

            List<String> gameList = getGameStrings(allGames);
            return String.join("\n" ,gameList);
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String logout(String... params) throws FacadeException {
        if (params.length != 0) {
            throw new FacadeException(400, "Logout needs no additional information.");
        }
        try {
            server.logoutUser(authToken);
            return "Successfully logged out.";
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String help() {
        return """
                    create <NAME> - create a new game
                    join <ID> <WHITE/BLACK> - join a game
                    observe <ID> - watch a game
                    list - see a list of all games
                    logout - log out of current account
                    help - see all available commands
                    quit - shutdown the program
                """;
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
}
