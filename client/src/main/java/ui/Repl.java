package ui;

import java.util.Scanner;

public class Repl {
    private PreloginUI preloginUI;
    private PostloginUI postloginUI;
    private State state = State.LOGGEDOUT;
    private String authToken = null;

    public Repl(String serverUrl) {
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
        while ((!result.equals("quit")) && (state != State.LOGGEDOUT)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = postloginUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                if ((line.startsWith("logout")) && (postloginUI.getAuthToken() == null)) {
                    state = State.LOGGEDOUT;
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        if (state == State.LOGGEDOUT) {
            authToken = null;
            preloginUI = new PreloginUI(serverUrl);
            preloginRun(serverUrl);
        }
        else {
            System.out.println();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + "[" + state + "] " + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

}
