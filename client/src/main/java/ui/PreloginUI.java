package ui;

import java.util.Arrays;

import model.*;
import ui.facade.*;

public class PreloginUI {
    private final ServerFacade server;
    private String authToken = null;

    public PreloginUI(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) throws FacadeException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String register(String... params) throws FacadeException {
        if (params.length != 3) {
            throw new FacadeException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
        }
        try {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            UserData newUser = new UserData(username, password, email);
            AuthData authData = server.registerUser(newUser);
            authToken = authData.authToken();
            System.out.println(authToken);
            String visitorName = String.join("-", username);
            return String.format("Successfully registered as %s.", visitorName);
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String... params) throws FacadeException {
        if (params.length != 2) {
            throw new FacadeException(400, "Expected: <USERNAME> <PASSWORD>");
        }
        try {
            var username = params[0];
            var password = params[1];
            UserData userData = new UserData(username, password, null);
            AuthData authData = server.loginUser(userData);
            authToken = authData.authToken();
            String visitorName = String.join("-", username);
            return String.format("Successfully logged in as %s.", visitorName);
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    public String help() {
        return """
                    register <USERNAME> <PASSWORD> <EMAIL> - create an account
                    login <USERNAME> <PASSWORD> - load a previously created account
                    help - see all available commands
                    quit - shutdown the program
                """;
    }

    public String getAuthToken() {
        return authToken;
    }
}
