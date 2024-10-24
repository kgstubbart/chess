package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    static final UserService service = new UserService(new MemoryUserDataAccess(), new MemoryAuthDataAccess());

    @BeforeEach
    void clear() throws ServiceException {
        service.clearUsers();
    }

    @Test
    void positive_registerUser() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        AuthData authData = service.registerUser(userData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negative_registerUser() throws ServiceException {
        UserData userData = new UserData("cheese", null, "crackers@mail");

        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> service.registerUser(userData),
            "Expected ServiceException due to invalid user data."
        );
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void positive_loginUser() throws ServiceException {
        UserData userData = new UserData("apple", "banana", "pear@mail");
        service.registerUser(userData);
        UserData userLoginData = new UserData("apple", "banana", null);
        AuthData authData = service.loginUser(userLoginData);

        assertEquals(userData.username(), authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void negative_loginUser() throws ServiceException {
        UserData userData = new UserData("cheese", "bread", "crackers@mail");
        service.registerUser(userData);
        UserData userLoginData = new UserData("cheese", "banana", null);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> service.loginUser(userLoginData),
                "Expected ServiceException due to invalid user data."
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void positive_logoutUser() {
    }

    @Test
    void negative_logoutUser() {
    }

    @Test
    void clearUsers() {
    }
}
