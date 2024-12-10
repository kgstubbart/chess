package websocket.messages;

public class LoadGameMessage<G> extends ServerMessage {
    private final G game;
    public LoadGameMessage(ServerMessageType type, G game) {
        super(type);
        this.game = game;
    }

    public G getGame() {
        return game;
    }
}
