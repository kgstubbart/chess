package server;

import com.google.gson.Gson;
import dataaccess.UserDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.UserData;
import spark.*;
import service.UserService;

import java.util.Map;

public class Server {
    private final UserDataAccess userDataAccess = new MemoryUserDataAccess();
    private final UserService user_service = new UserService(userDataAccess);
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.delete("/db", this::clearApplication);
        Spark.exception(Exception.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String createUser(Request request, Response response) throws Exception {
        var newUser = serializer.fromJson(request.body(), UserData.class);
        var result = user_service.registerUser(newUser);
        return serializer.toJson(result);
    }

    private String clearApplication(Request request, Response response) throws Exception {
        user_service.clearUsers();
        response.status(200);
        return "";
    }

    private void exceptionHandler(Exception exception, Request request, Response response) {
        response.status(500);
        response.body(serializer.toJson(Map.of("message", exception.getMessage())));
        exception.printStackTrace(System.err);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
