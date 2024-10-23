package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

public interface DataAccess {
    UserData getUser(String userName);
}
