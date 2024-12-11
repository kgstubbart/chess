package ui.facade;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
          try {
            throw new FacadeException(ex.getMessage());
          } catch (FacadeException e) {
            throw new RuntimeException(e);
          }
        }
    }

    //Endpoint requires this method
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void enterGameplay(String authToken, String username, Integer gameID) throws FacadeException {
        try {
            var command = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new FacadeException(ex.getMessage());
        }
    }

    public void leaveGameplay(String authToken, String username, Integer gameID) throws FacadeException {
        try {
            var command = new LeaveCommand(UserGameCommand.CommandType.LEAVE, authToken, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException ex) {
            throw new FacadeException(ex.getMessage());
        }
    }

    public void makeMove(String authToken, String username, Integer gameID, ChessMove move) throws FacadeException {
        try {
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, username, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new FacadeException(ex.getMessage());
        }
    }

}
