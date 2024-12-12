package ui;

import chess.ChessGame;
import ui.facade.FacadeException;
import ui.facade.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {
    private PreloginUI preloginUI;
    private PostloginUI postloginUI;
    private GameplayUI gameplayUI;
    private State state = State.LOGGEDOUT;
    private String authToken = null;
    private String username = null;
    private Integer gameID;
    private ChessGame game;
    private ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
    private String lastMove;

    public Repl (String serverUrl) {
        preloginUI = new PreloginUI(serverUrl);
    }

    public void preloginRun(String serverUrl) {
        System.out.println("Welcome to Chess! Read the following information to get started:");
        System.out.print(preloginUI.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((!result.equals("quit")) && (state != State.LOGGEDIN)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preloginUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                if ((line.startsWith("register") || line.startsWith("login")) && (preloginUI.getAuthToken() != null)) {
                    state = State.LOGGEDIN;
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        if (state == State.LOGGEDIN) {
            authToken = preloginUI.getAuthToken();
            username = preloginUI.getUserName();
            postloginRun(serverUrl);
        }
        else {
            System.out.println();
        }
    }

    public void postloginRun(String serverUrl) {
        postloginUI = new PostloginUI(serverUrl, authToken, username, this);
        System.out.print("\n" + postloginUI.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((state == State.LOGGEDIN)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = postloginUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                if ((line.startsWith("logout")) && (postloginUI.getAuthToken() == null)) {
                    state = State.LOGGEDOUT;
                }
                if ((line.startsWith("join") || line.startsWith("observe")) && (postloginUI.getInGame())) {
                    state = State.INGAME;
                    if (line.endsWith("black")) {
                        this.color = ChessGame.TeamColor.BLACK;
                    }
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        if (state == State.LOGGEDOUT) {
            authToken=null;
            preloginUI=new PreloginUI(serverUrl);
            preloginRun(serverUrl);
        }
        if (state == State.INGAME) {
            gameID = postloginUI.getGameID();
            gameplayRun(serverUrl);
        }
    }

    public void gameplayRun(String serverUrl) {
        gameplayUI = new GameplayUI(serverUrl, authToken, username, this, gameID, game, color, null);
        System.out.print("\n" + gameplayUI.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((state == State.INGAME)) {
            printPrompt();
            String line = scanner.nextLine();
            gameplayUI = new GameplayUI(serverUrl, authToken, username, this, gameID, game, color, scanner);
            try {
                if ((line.startsWith("resign"))) {
                    if (!line.equals("resign")) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Resign needs no additional information."
                                + EscapeSequences.RESET_TEXT_COLOR + "\n");
                    }
                    gameplayResignRun(serverUrl);
                    return;
                } else if ((line.startsWith("move"))) {
                    this.lastMove = line.substring(line.length() - 2);
                    result = gameplayUI.eval(line);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
                } else {
                    result = gameplayUI.eval(line);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
                }

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            if ((line.startsWith("leave"))) {
                state = State.LOGGEDIN;
            }
        }
        if (state == State.LOGGEDIN) {
            postloginRun(serverUrl);
        }
    }

    private void gameplayResignRun(String serverUrl) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printConfirmationPrompt();
                String line = scanner.nextLine().toLowerCase();

                if (line.equals("yes")) {
                    gameplayUI.resign("yes");
                    gameplayRun(serverUrl);
                } else if (line.equals("no")) {
                    gameplayRun(serverUrl);
                } else {
                    System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid resignation confirmation. Type <yes> or <no>"
                            + EscapeSequences.RESET_TEXT_COLOR);
                }
            }
        } catch (FacadeException e) {
            throw new RuntimeException(e);
        }
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + "[" + state + "] " + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    private void printConfirmationPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_MAGENTA + "    <Confirm: yes/no> " + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    private void loadGame(ChessGame game) {
        this.game = game;
        ChessBoard.createBoard(game.getBoard(), color, null);
        printPrompt();
    }

    private void displayError(String errorMessage) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_RED + "Error: " + errorMessage + EscapeSequences.RESET_TEXT_COLOR + "\n");
        printPrompt();
    }

    private void displayNotification(String message) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_YELLOW + "Notification: " + message + EscapeSequences.RESET_TEXT_COLOR + "\n");
        printPrompt();
    }
}
