package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.MySqlAuthDataAccess;
import dataaccess.MySqlGameDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ServiceException;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
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
            if (username == null) {
                connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Bad user authorization."));
            }

            connections.add(username, session);
            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> {
                    ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                    connect(session, username, connectCommand);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(session, makeMoveCommand);
                }
                case LEAVE -> {
                    LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    leave(session, username, leaveCommand);
                }
                case RESIGN -> {
                    ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                    resign(session, resignCommand);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage errorMessage) {
    }

    private void resign(Session session, ResignCommand command) {
    }

    private void leave(Session session, String username, LeaveCommand command) throws IOException {
        connections.remove(username);
        var message = String.format("%s has left the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }

    private void makeMove(Session session, MakeMoveCommand command) {
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException, ServiceException {
        connections.add(username, session);
        var message = String.format("%s is in the game.", username);
        var gameMessage = getGameData(session, command.getGameID());
        if (gameMessage != null) {
            var broadcastNotification=new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var userLoadGame=new LoadGameMessage<>(ServerMessage.ServerMessageType.LOAD_GAME, gameMessage);
            connections.userBroadcast(session, userLoadGame);
            connections.broadcast(username, broadcastNotification);
        }
    }

    private ChessGame getGameData(Session session, Integer gameID) throws ServiceException, IOException {
        GameData gameData = new MySqlGameDataAccess().getGame(gameID);
        if (gameData == null) {
            var userError = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "This game does not exist.");
            connections.userBroadcast(session, userError);
            return null;
        }
        else {
            return gameData.game();
        }
    }

    private void saveSession(Integer gameID, Session session) {

    }

    private String getUsername(String authToken) throws ServiceException {
        AuthData authData = new MySqlAuthDataAccess().getAuth(authToken);
        if (authData == null) {
            return null;
        }
        else {
            return authData.username();
        }
    }

}
