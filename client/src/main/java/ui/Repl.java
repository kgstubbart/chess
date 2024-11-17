package ui;

import java.util.Scanner;

public class Repl {
    private final PreloginUI preloginUI;

    public Repl(String serverUrl) {
        preloginUI = new PreloginUI(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess! Read the following information to get started:");
        System.out.print(preloginUI.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preloginUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

}
