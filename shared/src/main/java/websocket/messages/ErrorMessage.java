package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;
    public ErrorMessage(String type, String errorMessage) {
        super(ServerMessageType.valueOf(type));
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
