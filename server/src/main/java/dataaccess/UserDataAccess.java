package dataaccess;

import model.UserData;
import service.ServiceException;

public interface UserDataAccess {
    UserData getUser(String userName, String password) throws ServiceException;

    void createUser(UserData userData) throws ServiceException;

    void clear() throws ServiceException;
}
