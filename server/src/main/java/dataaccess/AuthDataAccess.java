package dataaccess;

import model.AuthData;
import model.UserData;
import service.ServiceException;

public interface AuthDataAccess {
    String createAuth(UserData userData) throws ServiceException;

    AuthData getAuth(String authToken);

    void deleteAuth(AuthData authData);

    void clear();
}
