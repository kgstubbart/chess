package ui;

import java.util.Arrays;

import model.*;
import ui.facade.*;

public class PreloginUI {
    private final ServerFacade server;
    private String authToken = null;
    private String userName = null;

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
            return EscapeSequences.SET_TEXT_COLOR_RED + "Expected: <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            UserData newUser = new UserData(username, password, email);
            AuthData authData = server.registerUser(newUser);
            authToken = authData.authToken();
            userName = username;
            String visitorName = String.join("-", username);
            return String.format("Successfully registered as %s." + "\n", visitorName);
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String login(String... params) throws FacadeException {
        if (params.length != 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Expected: <USERNAME> <PASSWORD>" + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            var username = params[0];
            var password = params[1];
            UserData userData = new UserData(username, password, null);
            AuthData authData = server.loginUser(userData);
            authToken = authData.authToken();
            userName = username;
            String visitorName = String.join("-", username);
            return String.format("Successfully logged in as %s." + "\n", visitorName);
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    public String help() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "    register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - create an account" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - load a previously created account" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    help" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - see all available commands" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    quit" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - shutdown the program" + EscapeSequences.RESET_TEXT_COLOR + "\n";
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }
}
