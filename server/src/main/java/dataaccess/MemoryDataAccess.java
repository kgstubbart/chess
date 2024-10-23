package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}
