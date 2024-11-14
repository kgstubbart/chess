package ui.facade;

public class FacadeException extends Exception {
    public FacadeException(int code, String message) {
        super(code + ": " + message);
    }
}