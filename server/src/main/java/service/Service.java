package service;

import dataaccess.UserDataAccess;
import model.UserData;

public class Service {
    private final UserDataAccess userDataAccess;

    public Service(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public UserData registerUser(UserData newUser) throws ServiceException {
        if (userDataAccess.getUser(newUser.username()) != null) {
            throw new ServiceException("User already exists");
        }

        userDataAccess.saveUser(newUser);
        return newUser;
    }
}
