package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.UserData;
import spark.*;
import service.Service;

import java.util.Map;

public class Server {
    private final DataAccess dataAccess = new MemoryDataAccess();
    private final Service service = new Service(dataAccess);
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.exception(Exception.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String createUser(Request request, Response response) throws Exception {
        var newUser = serializer.fromJson(request.body(), UserData.class);
        var result = service.registerUser(newUser);
        return serializer.toJson(result);
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
