package ui.facade;

import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData registerUser(UserData userData) throws FacadeException {
        var path = "/user";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public AuthData loginUser(UserData userData) throws FacadeException {
        var path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public void logoutUser(String authData) throws FacadeException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authData);
    }

    public GameData[] listGames() throws FacadeException {
        var path = "/game";
        record listGameResponse(GameData[] gameData) {
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class, null);
        return response.gameData();
    }

    public GameData createGame(GameData gameData, String authData) throws FacadeException {
        var path = "/game";
        return this.makeRequest("POST", path, gameData, GameData.class, authData);
    }

    public GameData joinGame(JoinGameData joinGameData) throws FacadeException {
        var path = "/game";
        return this.makeRequest("PUT", path, joinGameData, GameData.class, null);
    }

    private void clearApplication() throws FacadeException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws FacadeException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", authToken);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new FacadeException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, FacadeException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new FacadeException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
