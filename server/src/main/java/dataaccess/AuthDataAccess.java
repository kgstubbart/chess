package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDataAccess {
    String createAuth(UserData userData);

    AuthData getAuth(String authToken);

    void deleteAuth(AuthData authData);

    void clear();
}
