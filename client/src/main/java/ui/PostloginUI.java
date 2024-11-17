package ui;

import java.util.Arrays;

import model.*;
import ui.facade.*;

public class PostloginUI {
    private final ServerFacade server;

    public PostloginUI(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) throws FacadeException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "quit" -> "quit";
            default -> help();
        };
    }

//    public String register(String... params) throws FacadeException {
//        if (params.length != 3) {
//            throw new FacadeException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
//        }
//        try {
//            var username = params[0];
//            var password = params[1];
//            var email = params[2];
//            UserData newUser = new UserData(username, password, email);
//            server.registerUser(newUser);
//            String visitorName = String.join("-", username);
//            return String.format("Successfully registered as %s.", visitorName);
//        } catch (FacadeException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
