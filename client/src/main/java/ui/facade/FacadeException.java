package ui.facade;

import ui.EscapeSequences;

public class FacadeException extends Exception {
    public FacadeException(String message) {
        super(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.RESET_TEXT_COLOR);
    }
}