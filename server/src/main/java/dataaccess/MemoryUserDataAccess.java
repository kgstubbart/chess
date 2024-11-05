package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDataAccess implements UserDataAccess {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username, String password) {
        return users.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
