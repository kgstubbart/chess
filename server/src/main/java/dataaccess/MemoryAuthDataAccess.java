package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDataAccess {
    final private Map<String, UserData> auths = new HashMap<>();

    @Override
    public String createAuth(UserData userData) {
        String authToken = UUID.randomUUID().toString();
        auths.put(authToken, userData);
        new AuthData(authToken, userData.username());
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        UserData userData = auths.get(authToken);
        return new AuthData(authToken, userData.username());
    }

    @Override
    public void deleteAuth(AuthData authData) {
        auths.remove(authData.authToken());
    }

    @Override
    public void clear() {
        auths.clear();
    }
}
