package websocket.commands;

public class ResignCommand extends UserGameCommand {
    private final String username;
    public ResignCommand(CommandType commandType, String authToken, String username, Integer gameID) {
        super(commandType, authToken, gameID);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
