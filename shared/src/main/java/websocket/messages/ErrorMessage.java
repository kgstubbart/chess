package websocket.messages;

public class ErrorMessage extends ServerMessage {
    public ErrorMessage(String type) {
        super(ServerMessageType.valueOf(type));
    }
}
