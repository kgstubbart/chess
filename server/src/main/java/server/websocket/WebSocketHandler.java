package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {
      private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leave(session, username, (LeaveCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage errorMessage) {
    }

    private void resign(Session session, String username, ResignCommand command) {
    }

    private void leave(Session session, String username, LeaveCommand command) throws IOException {
        connections.remove(username);
        var message = String.format("%s has left the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) {
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException {
        connections.add(username, session);
        var message = String.format("%s is in the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }

    private void saveSession(Integer gameID, Session session) {

    }

    private String getUsername(String authToken) {
        return null;
    }
}
