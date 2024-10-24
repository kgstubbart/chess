package dataaccess;

import model.UserData;

public interface UserDataAccess {
    UserData getUser(String userName);

    void createUser(UserData userData);

    void clear();
}
