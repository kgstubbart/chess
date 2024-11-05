package dataaccess;

import model.AuthData;
import model.UserData;
import service.ServiceException;

public interface AuthDataAccess {
    String createAuth(UserData userData) throws ServiceException;

    AuthData getAuth(String authToken) throws ServiceException;

    void deleteAuth(AuthData authData);

    void clear() throws ServiceException;
}
