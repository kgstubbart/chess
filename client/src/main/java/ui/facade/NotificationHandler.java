package ui.facade;

import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage notification);
}