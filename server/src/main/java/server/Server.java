package server;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.UserData;
import spark.*;
import service.UserService;

import java.util.Map;

public class Server {
    private final UserDataAccess userDataAccess = new MemoryUserDataAccess();
    private final AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    private final UserService user_service = new UserService(userDataAccess, authDataAccess);
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
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
