package service;

import dataaccess.UserDataAccess;
import model.UserData;

public class UserService {
    private final UserDataAccess userDataAccess;

    public UserService(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public UserData registerUser(UserData newUser) throws ServiceException {
        if (userDataAccess.getUser(newUser.username()) != null) {
            throw new ServiceException("User already exists");
        }

        userDataAccess.saveUser(newUser);
        return newUser;
    }

    public void clearUsers() throws ServiceException {
        userDataAccess.clear();
    }
}
