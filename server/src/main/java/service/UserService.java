package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    final UserDataAccess userDataAccess;
    final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public AuthData registerUser(UserData newUser) throws ServiceException {
        if (newUser == null || newUser.username() == null || newUser.username().isEmpty() || newUser.password() == null
                || newUser.password().isEmpty() || newUser.email() == null || newUser.email().isEmpty()) {
            throw new ServiceException("Error: bad request");
        }

        if (userDataAccess.getUser(newUser.username()) != null) {
            throw new ServiceException("Error: already taken");
        }

        userDataAccess.createUser(newUser);
        String authToken = authDataAccess.createAuth(newUser);
        return authDataAccess.getAuth(authToken);
    }

    public AuthData loginUser(UserData user) throws ServiceException {
        if ((userDataAccess.getUser(user.username()) == null) ||
                (!Objects.equals(user.password(), userDataAccess.getUser(user.username()).password()))) {
            throw new ServiceException("Error: unauthorized");
        }

        String authToken = authDataAccess.createAuth(user);
        return authDataAccess.getAuth(authToken);
    }

    public void logoutUser(String authToken) throws ServiceException {
        if (authDataAccess.getAuth(authToken) == null) {
            throw new ServiceException("Error: unauthorized");
        }

        AuthData authorization = authDataAccess.getAuth(authToken);
        authDataAccess.deleteAuth(authorization);
    }

    public void clearUsers() throws ServiceException {
        userDataAccess.clear();
        authDataAccess.clear();
    }
}
