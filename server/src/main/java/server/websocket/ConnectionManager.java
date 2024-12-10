package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Session, String> sessions = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
        sessions.put(session, username);
    }

    public void remove(String username) {
        var connection = connections.remove(username);
        sessions.remove(connection.session);
    }

    public void remove(Session session) {
        var username = sessions.remove(session);
        connections.remove(username);
    }

    public void broadcast(String excludeUsername, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void userBroadcast(Session session, ServerMessage message) throws IOException {
        var username = sessions.get(session);
        if (username == null) {
            sendErrorThroughSession(session, "Bad user authorization.");
        }

        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    c.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    private void sendErrorThroughSession(Session session, String errorMessage) throws IOException {
        var message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
        session.getRemote().sendString(new Gson().toJson(message));
    }
}
