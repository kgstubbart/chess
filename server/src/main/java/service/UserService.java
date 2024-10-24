package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

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
        if (userDataAccess.getUser(user.username()) == null) {
            throw new ServiceException("Error: unauthorized");
        }

        String authToken = authDataAccess.createAuth(user);
        return authDataAccess.getAuth(authToken);
    }

    public void clearUsers() throws ServiceException {
        userDataAccess.clear();
    }
}
