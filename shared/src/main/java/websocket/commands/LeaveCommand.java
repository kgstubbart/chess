package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    private final String username;

    public LeaveCommand(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, gameID);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
