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
        try {
            var path="/user";
            return this.makeRequest("POST", path, userData, AuthData.class, null);
        } catch (FacadeException e) {
            throw new FacadeException(400, "Error: bad request");
        }
    }

    public AuthData loginUser(UserData userData) throws FacadeException {
        var path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public void logoutUser(String authToken) throws FacadeException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData[] listGames(String authToken) throws FacadeException {
        var path = "/game";
        record listGameResponse(GameData[] games) {
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class, authToken);
        return response.games();
    }

    public void createGame(GameData gameData, String authToken) throws FacadeException {
        var path = "/game";
        this.makeRequest("POST", path, gameData, GameData.class, authToken);
    }

    public void joinGame(JoinGameData joinGameData, String authToken) throws FacadeException {
        var path = "/game";
        this.makeRequest("PUT", path, joinGameData, GameData.class, authToken);
    }

    public void clearApplication() throws FacadeException {
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
