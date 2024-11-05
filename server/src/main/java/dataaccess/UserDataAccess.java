package dataaccess;

import model.UserData;
import service.ServiceException;

public interface UserDataAccess {
    UserData getUser(String userName);

    void createUser(UserData userData) throws ServiceException;

    void clear();
}
