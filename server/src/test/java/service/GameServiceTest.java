package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import org.junit.jupiter.api.Test;

class GameServiceTest {
    static final GameService service = new GameService(new MemoryGameDataAccess(), new MemoryAuthDataAccess());

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void clearGames() {
    }
}
