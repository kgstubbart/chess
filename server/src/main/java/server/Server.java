package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import service.GameService;
import spark.*;
import service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final UserDataAccess userDataAccess = new MemoryUserDataAccess();
    private final AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    private final GameDataAccess gameDataAccess = new MemoryGameDataAccess();
    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final GameService gameService = new GameService(gameDataAccess, authDataAccess);
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
        Spark.get("/game", this::listGames);
        Spark.delete("/db", this::clearApplication);
        Spark.exception(Exception.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String registerUser(Request request, Response response) throws Exception {
        var newUser = serializer.fromJson(request.body(), UserData.class);
        var result = userService.registerUser(newUser);
        return serializer.toJson(result);
    }

    private String loginUser(Request request, Response response) throws Exception {
        var user = serializer.fromJson(request.body(), UserData.class);
        var result = userService.loginUser(user);
        return serializer.toJson(result);
    }

    private String logoutUser(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        userService.logoutUser(authToken);
        response.status(200);
        return "";
    }

    private String createGame(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        var gameName = serializer.fromJson(request.body(), GameData.class);
        var result = gameService.createGame(authToken, gameName);
        return serializer.toJson(result);
    }

    private String joinGame(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        var joinGameData = serializer.fromJson(request.body(), JoinGameData.class);
        var result = gameService.joinGame(authToken, joinGameData);
        return serializer.toJson(result);
    }

    private String listGames(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        Collection<GameData> result = gameService.listGames(authToken);
        Map<String, Object> mapList = new HashMap<>();
        mapList.put("games", result);
        return serializer.toJson(mapList);
    }

    private String clearApplication(Request request, Response response) throws Exception {
        userService.clearUsers();
        gameService.clearGames();
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
