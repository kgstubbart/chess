package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.MySqlAuthDataAccess;
import dataaccess.MySqlGameDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.ServiceException;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


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

            connections.add(username, command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> {
                    ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                    connect(session, username, connectCommand);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(session, username, makeMoveCommand);
                }
                case LEAVE -> {
                    LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    leave(session, username, leaveCommand);
                }
                case RESIGN -> {
                    ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                    resign(session, username, resignCommand);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void resign(Session session, String username, ResignCommand command) throws IOException, ServiceException {
        Integer gameID = command.getGameID();
        GameData gameData = new MySqlGameDataAccess().getGame(gameID);
        if ((!Objects.equals(username, gameData.whiteUsername())) && (!Objects.equals(username, gameData.blackUsername()))) {
            connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not playing game."));
            return;
        }
        if (connections.finishGames.containsKey(gameID)) {
            connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Game already finished."));
            return;
        }
        var message = String.format("%s has resigned the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, gameID, notification);
        connections.userBroadcast(session, notification);
        connections.remove(session);
        connections.finish(gameID, username);
    }

    private void leave(Session session, String username, LeaveCommand command) throws IOException, ServiceException {
        Integer gameID = command.getGameID();
        GameData gameData = new MySqlGameDataAccess().getGame(gameID);
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        String gameName = gameData.gameName();
        ChessGame game = gameData.game();
        if (Objects.equals(whiteUsername, username)) {
            gameData = new GameData(gameID, null, blackUsername, gameName, game);
            new MySqlGameDataAccess().updateGame(null, ChessGame.TeamColor.WHITE, gameID, gameData);
        }
        if (Objects.equals(blackUsername, username)) {
            gameData = new GameData(gameID, whiteUsername, null, gameName, game);
            new MySqlGameDataAccess().updateGame(null, ChessGame.TeamColor.BLACK, gameID, gameData);
        }
        connections.remove(username);
        var message = String.format("%s has left the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, gameID, notification);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws ServiceException, IOException, InvalidMoveException {
        Integer gameID = command.getGameID();
        ChessGame game = getGameData(session, gameID);
        assert game != null;
        if (connections.finishGames.containsKey(gameID)) {
            connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Game already finished."));
            return;
        }
        String whiteUsername = new MySqlGameDataAccess().getGame(gameID).whiteUsername();
        String blackUsername = new MySqlGameDataAccess().getGame(gameID).blackUsername();
        String gameName = new MySqlGameDataAccess().getGame(gameID).gameName();
        ChessGame.TeamColor userColor = null;
        if (Objects.equals(whiteUsername, username)) {
            userColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(blackUsername, username)) {
            userColor = ChessGame.TeamColor.BLACK;
        } else {
            connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "User not in game."));
            return;
        }
        if (game.getTeamTurn() != userColor) {
            connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not your turn."));
            return;
        }
        ChessMove move = command.getMove();
        ChessPosition startPos = move.getStartPosition();
        Collection<ChessMove> validMoves = game.validMoves(startPos);
        boolean valid = false;
        for (ChessMove m : validMoves) {
            if (m.equals(move)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            connections.userBroadcast(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid move."));
            return;
        }
        game.makeMove(move);
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        new MySqlGameDataAccess().updateGame(username, userColor, gameID, gameData);
        var userLoadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        var broadcastLoadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        var message = String.format("Opponent moved to %s.", move.getEndPosition());
        var broadcastNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.userBroadcast(session, userLoadGame);
        connections.broadcast(username, gameID, broadcastLoadGame);
        connections.broadcast(username, gameID, broadcastNotification);
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            connections.userBroadcast(session, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Game is over."));
            connections.broadcast(username, gameID, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Game is over."));
            connections.finish(gameID, username);
            return;
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException, ServiceException {
        connections.add(username, command.getGameID(), session);
        var message = String.format("%s is in the game.", username);
        var gameMessage = getGameData(session, command.getGameID());
        if (gameMessage != null) {
            var broadcastNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var userLoadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameMessage);
            connections.userBroadcast(session, userLoadGame);
            connections.broadcast(username, command.getGameID(), broadcastNotification);
        }
    }

    private ChessGame getGameData(Session session, Integer gameID) throws ServiceException, IOException {
        GameData gameData = new MySqlGameDataAccess().getGame(gameID);
        if (gameData == null) {
            var userError = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "This game does not exist.");
            connections.userBroadcast(session, userError);
            return null;
        } else {
            return gameData.game();
        }
    }

    private String getUsername(String authToken) throws ServiceException {
        AuthData authData = new MySqlAuthDataAccess().getAuth(authToken);
        if (authData == null) {
            return null;
        } else {
            return authData.username();
        }
    }
}
