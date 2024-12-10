package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private final String username;

    public ConnectCommand(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, gameID);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
