package ui;

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
            postloginRun(serverUrl);
        }
        else {
            System.out.println();
        }
    }

    public void postloginRun(String serverUrl) {
        postloginUI = new PostloginUI(serverUrl, authToken);
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
                if ((line.startsWith("join") || line.startsWith("observe"))) {
                    state = State.INGAME;
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
            gameplayRun(serverUrl);
        }
    }

    public void gameplayRun(String serverUrl) {
        gameplayUI = new GameplayUI(serverUrl, authToken);
        System.out.print("\n" + gameplayUI.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((state == State.INGAME)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = gameplayUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

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

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + "[" + state + "] " + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    private void loadGame(String game) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + game + EscapeSequences.RESET_TEXT_COLOR);
    }

    private void displayError(String errorMessage) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_RED + "Error: " + errorMessage + EscapeSequences.RESET_TEXT_COLOR);
    }

    private void displayNotification(String message) {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_YELLOW + "Notification: " + message + EscapeSequences.RESET_TEXT_COLOR);
    }
}
