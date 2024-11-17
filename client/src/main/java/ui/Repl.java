package ui;

import java.util.Scanner;

public class Repl {
    private final PreloginUI preloginUI;
    private State state = State.LOGGEDOUT;

    public Repl(String serverUrl) {
        preloginUI = new PreloginUI(serverUrl);
    }

    public void preloginRun() {
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

                if (line.startsWith("register") || line.startsWith("login")) {
                    state = State.LOGGEDIN;
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        if (state == State.LOGGEDIN) {
            postloginRun();
        }
        else {
            System.out.println();
        }
    }

    public void postloginRun() {
        System.out.println("\n" + "WOULD NOW BE IN POST");
        System.out.print(preloginUI.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((!result.equals("quit")) && (state != State.LOGGEDOUT)) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preloginUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                if (line.startsWith("register") || line.startsWith("login")) {
                    state = State.LOGGEDIN;
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        if (state == State.LOGGEDOUT) {
            //
        }
        else {
            System.out.println();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + "[" + state + "] " + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

}
