package ui;

import ui.facade.FacadeException;
import ui.facade.NotificationHandler;
import ui.facade.ServerFacade;
import ui.facade.WebSocketFacade;

import java.util.Arrays;

public class GameplayUI {
    private final WebSocketFacade webSocket;
    private String authToken;

    public GameplayUI(String serverUrl, String authToken, NotificationHandler notificationHandler) {
        webSocket = new WebSocketFacade(serverUrl, notificationHandler);
        this.authToken = authToken;
    }

    public String eval(String input) throws FacadeException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "move" -> move(params);
            case "highlight" -> highlight(params);
            case "redraw" -> redraw(params);
            case "resign" -> resign(params);
            case "leave" -> leave(params);
            default -> help();
        };
    }

    public String move(String... params) throws FacadeException {
        return "";
    }

    public String redraw(String... params) throws FacadeException {
        return "";
    }

    public String highlight(String... params) throws FacadeException {
        return "";
    }

    public String resign(String... params) throws FacadeException {
        return "";
    }

    public String leave(String... params) throws FacadeException {
        return "";
    }

    public String help() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "    move <CURRENT_POSITION> <NEW_POSITION>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - move a piece on the board" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    highlight <POSITION>" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - all legal moves of the selected piece are highlighted" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    redraw" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - redraws the chess board" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    resign" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - forfeit the game" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    leave" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - remove yourself from the game" + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "    help" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY +
                " - see all available in-game commands" + EscapeSequences.RESET_TEXT_COLOR + "\n";
    }

    public String getAuthToken() {
        return authToken;
    }
}
