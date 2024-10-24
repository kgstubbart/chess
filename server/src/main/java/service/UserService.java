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
        if (userDataAccess.getUser(newUser.username()) != null) {
            throw new ServiceException("User already exists");
        }

        userDataAccess.createUser(newUser);
        String authToken = authDataAccess.createAuth(newUser);
        return authDataAccess.getAuth(authToken);
    }

    public void clearUsers() throws ServiceException {
        userDataAccess.clear();
    }
}
