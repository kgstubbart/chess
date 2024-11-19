package ui.facade;

import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData registerUser(UserData userData) throws FacadeException {
        try {
            var path = "/user";
            return this.makeRequest("POST", path, userData, AuthData.class, null);
        } catch (FacadeException e) {
            throw new FacadeException("Error: bad register request");
        }
    }

    public AuthData loginUser(UserData userData) throws FacadeException {
        try {
            var path = "/session";
            return this.makeRequest("POST", path, userData, AuthData.class, null);
        } catch (FacadeException e) {
            throw new FacadeException("Error: bad login request");
        }
    }

    public void logoutUser(String authToken) throws FacadeException {
        try {
            var path = "/session";
            this.makeRequest("DELETE", path, null, null, authToken);
        } catch (FacadeException e) {
            throw new FacadeException("Error: bad logout request");
        }
    }

    public GameData[] listGames(String authToken) throws FacadeException {
        try {
            var path = "/game";
            record listGameResponse(GameData[] games) {
            }
            var response = this.makeRequest("GET", path, null, listGameResponse.class, authToken);
            return response.games();
        } catch (FacadeException e) {
            throw new FacadeException("Error: bad list request");
        }
    }

    public GameData createGame(GameData gameData, String authToken) throws FacadeException {
        try {
            var path = "/game";
            return this.makeRequest("POST", path, gameData, GameData.class, authToken);
        } catch (FacadeException e) {
            throw new FacadeException("Error: bad create request");
        }
    }

    public GameData joinGame(JoinGameData joinGameData, String authToken) throws FacadeException {
        try {
            var path = "/game";
            return this.makeRequest("PUT", path, joinGameData, GameData.class, authToken);
        } catch (FacadeException e) {
            throw new FacadeException("Error: bad join request");
        }
    }

    public void clearApplication(String clearPassword) throws FacadeException {
        if (Objects.equals(clearPassword, "mandostormsslug")) {
            var path = "/db";
            this.makeRequest("DELETE", path, null, null, null);
        } else {
            throw new FacadeException("Error: bad clear request");
        }
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
            throw new FacadeException(ex.getMessage());
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
            throw new FacadeException("failure: " + status);
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
