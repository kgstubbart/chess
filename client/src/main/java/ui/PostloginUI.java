package ui;

import java.util.Arrays;

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
            case "logout" -> logout(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String logout(String... params) throws FacadeException {
        if (params.length != 0) {
            throw new FacadeException(400, "Logout needs no additional information.");
        }
        try {
            System.out.println(authToken);
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
}
