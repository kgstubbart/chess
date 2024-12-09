package ui;

import chess.ChessMove;
import chess.ChessPosition;
import ui.facade.FacadeException;
import ui.facade.NotificationHandler;
import ui.facade.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameplayUI {
    private final WebSocketFacade webSocket;
    private String authToken;
    private String username;
    private Integer gameID;

    public GameplayUI(String serverUrl, String authToken, String username, NotificationHandler notificationHandler, Integer gameID) {
        webSocket = new WebSocketFacade(serverUrl, notificationHandler);
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
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
        if (params.length != 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Move needs two chess positions." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            String startPosStr= params[0];
            String endPosStr= params[1];
            ChessPosition startPos = convertPosition(startPosStr);
            ChessPosition endPos = convertPosition(endPosStr);
            ChessMove move = new ChessMove(startPos, endPos, null); // need to change from null

            webSocket.makeMove(authToken, username, gameID, move);
            return """
                    Successfully left game.
                    
                    """;
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
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
        if (params.length != 0) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Leave needs no additional information." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            webSocket.leaveGameplay(authToken, username, gameID);
            return """
                    Successfully left game.
                    
                    """;
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
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

    public ChessPosition convertPosition(String position) {
        char col_str = position.charAt(0);
        char row_str = position.charAt(1);

        int col = col_str - 'a' + 1;
        int row = Character.getNumericValue(row_str);

        return new ChessPosition(col, row);
    }
}
