package tasktracker.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static Gson gson = new Gson();
    private static TaskManager manager = Managers.getDefaultFile();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new HttpTaskServerHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class HttpTaskServerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] pathElements = path.split("/");
            String lastPathElement = pathElements[pathElements.length - 1];
            String endpoint = method + " /" + lastPathElement;
            int rCode = 404;
            String response = "";

            switch (endpoint) {
                case "GET /tasks":
                    rCode = 200;
                    response = gson.toJson(manager.getListAllTasks());
                    break;
            }

            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static {
        Task task1 = new Task("task1", "desc");
        manager.createTask(task1);
    }
}
