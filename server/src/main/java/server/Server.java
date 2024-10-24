package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import model.UserData;
import service.GameService;
import spark.*;
import service.UserService;

import java.util.Map;

public class Server {
    private final UserDataAccess userDataAccess = new MemoryUserDataAccess();
    private final AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    private final GameDataAccess gameDataAccess = new MemoryGameDataAccess();
    private final UserService user_service = new UserService(userDataAccess, authDataAccess);
    private final GameService game_service = new GameService(gameDataAccess, authDataAccess);
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearApplication);
        Spark.exception(Exception.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String registerUser(Request request, Response response) throws Exception {
        var newUser = serializer.fromJson(request.body(), UserData.class);
        var result = user_service.registerUser(newUser);
        return serializer.toJson(result);
    }

    private String loginUser(Request request, Response response) throws Exception {
        var user = serializer.fromJson(request.body(), UserData.class);
        var result = user_service.loginUser(user);
        return serializer.toJson(result);
    }

    private String logoutUser(Request request, Response response) throws Exception {
        String auth_token = request.headers("Authorization");
        user_service.logoutUser(auth_token);
        response.status(200);
        return "";
    }

    private String createGame(Request request, Response response) throws Exception {
        String auth_token = request.headers("Authorization");
        var gameName = serializer.fromJson(request.body(), GameData.class);
        var result = game_service.createGame(auth_token, gameName);
        return serializer.toJson(result);
    }

    private String joinGame(Request request, Response response) throws Exception {
        String auth_token = request.headers("Authorization");
        var gameBaseData = serializer.fromJson(request.body(), Map.class);
        String playerColor = (String) gameBaseData.get("playerColor");
        int gameID = ((Double) gameBaseData.get("gameID")).intValue();
        var result = game_service.joinGame(auth_token, playerColor, gameID);
        return serializer.toJson(result);
    }

    private String clearApplication(Request request, Response response) throws Exception {
        user_service.clearUsers();
        response.status(200);
        return "";
    }

    private void exceptionHandler(Exception exception, Request request, Response response) {
        String message = exception.getMessage();
        if (message.equals("Error: bad request")) {
            response.status(400);
            response.body(serializer.toJson(Map.of("message", message)));
        }
        else if (message.equals("Error: already taken")) {
            response.status(403);
            response.body(serializer.toJson(Map.of("message", message)));
        }
        else if (message.equals("Error: unauthorized")) {
            response.status(401);
            response.body(serializer.toJson(Map.of("message", message)));
        }
        else {
            response.status(500);
            response.body(serializer.toJson(Map.of("message", "Error: " + message)));
        }
        exception.printStackTrace(System.err);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
