package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameServiceTest {
    static final GameService service = new GameService(new MemoryGameDataAccess(), new MemoryAuthDataAccess());

    @BeforeEach
    void clear() throws ServiceException {
        service.clearGames();
    }

    @Test
    void positive_createGame() {
    }

    @Test
    void negative_createGame() {
    }

    @Test
    void positive_joinGame() {
    }

    @Test
    void negative_joinGame() {
    }

    @Test
    void positive_listGames() {
    }

    @Test
    void negative_listGames() {
    }

    @Test
    void clearGames() {
    }
}
