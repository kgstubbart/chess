package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.facade.FacadeException;
import ui.facade.NotificationHandler;
import ui.facade.WebSocketFacade;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class GameplayUI {
    private final WebSocketFacade webSocket;
    private String authToken;
    private String username;
    private Integer gameID;
    private ChessGame game;
    private ChessGame.TeamColor color;
    private Scanner scanner;

    public GameplayUI(String serverUrl, String authToken, String username, NotificationHandler notificationHandler,
                      Integer gameID, ChessGame game, ChessGame.TeamColor color, Scanner scanner) {
        webSocket = new WebSocketFacade(serverUrl, notificationHandler);
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.game = game;
        this.color = color;
        this.scanner = scanner;
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
        List<Character> validLetters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        List<Character> validNums = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8');
        if (params.length != 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Move needs two chess positions." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            String startPosStr = params[0];
            String endPosStr = params[1];
            if (startPosStr == null || startPosStr.length() != 2) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Positions must be in format letter|number. For example: e7." +
                        EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            if (endPosStr == null || endPosStr.length() != 2) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Positions must be in format letter|number. For example: e7." +
                        EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            char startLetter = startPosStr.charAt(0);
            char startNumber = startPosStr.charAt(1);
            char endLetter = endPosStr.charAt(0);
            char endNumber = endPosStr.charAt(1);
            if (!((validLetters.contains(startLetter)) && (validLetters.contains(endLetter))
                    && (validNums.contains(startNumber)) && (validNums.contains(endNumber)))) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Error: Positions must be in format letter|number. For example: e7." +
                        EscapeSequences.RESET_TEXT_COLOR + "\n";
            }
            ChessPosition startPos = convertPosition(startPosStr);
            ChessPiece piece = game.getBoard().getPiece(startPos);
            ChessPiece.PieceType promotionPiece = null;
            ChessPosition endPos = convertPosition(endPosStr);
            if ((piece.getPieceType() == ChessPiece.PieceType.PAWN) && (((endPos.getRow() == 0) &&
                    (piece.getTeamColor() == ChessGame.TeamColor.BLACK)) ||
                    ((endPos.getRow() == 7) && (piece.getTeamColor() == ChessGame.TeamColor.WHITE)))) {
                promotionPiece = getPromotion();
            }
            ChessMove move = new ChessMove(startPos, endPos, promotionPiece);

            webSocket.makeMove(authToken, username, gameID, move);
            return "";
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
    }

    private ChessPiece.PieceType getPromotion() {
        while (true) {
            printPromotionPrompt();
            String input = scanner.nextLine();
            var token = input.toLowerCase();
            switch(token) {
                case "queen" -> { return ChessPiece.PieceType.QUEEN; }
                case "rook" -> { return ChessPiece.PieceType.ROOK; }
                case "bishop" -> { return ChessPiece.PieceType.BISHOP; }
                case "knight" -> { return ChessPiece.PieceType.KNIGHT; }
            }
        }
    }

    private void printPromotionPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "    Choose piece to promote pawn to: <QUEEN/ROOK/BISHOP/KNIGHT> "
                + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public String redraw(String... params) {
        if (params.length != 0) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Redraw needs no additional information." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        ChessBoard.createBoard(game.getBoard(), color, null);
        return "\n";
    }

    public String highlight(String... params) throws FacadeException {
        List<Character> validLetters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        List<Character> validNums = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8');
        if (params.length != 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Move needs two chess positions." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        String startPosStr = params[0];
        if (startPosStr == null || startPosStr.length() != 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Positions must be in format letter|number. For example: e7." +
                    EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        char startLetter = startPosStr.charAt(0);
        char startNumber = startPosStr.charAt(1);
        if (!((validLetters.contains(startLetter)) && (validNums.contains(startNumber)))) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Error: Positions must be in format letter|number. For example: e7." +
                    EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        ChessPosition startPos = convertPosition(startPosStr);
        ChessBoard.validMovesBoard(game, color, startPos);
        return "\n";
    }

    public String resign(String... params) throws FacadeException {
        if (!Objects.equals(params[0], "yes")) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Resign needs no additional information." + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
        try {
            webSocket.resignGame(authToken, username, gameID);
            return """
                    Successfully resigned game.
                    
                    """;
        } catch (FacadeException e) {
            return e.getMessage() + "\n";
        }
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
        char colStr= position.charAt(0);
        char rowStr = position.charAt(1);

        int col = colStr - 'a' + 1;
        int row = Character.getNumericValue(rowStr);

        return new ChessPosition(row, col);
    }
}
