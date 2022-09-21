package tasktracker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Pattern;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static TaskManager manager = Managers.getDefaultFile();

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServerHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.stop(10);
    }

    static class HttpTaskServerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();
            int rCode = 404;
            String response = "";

            switch (method) {
                case "GET":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        rCode = 200;
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            response = gson.toJson(manager.getTaskById(id));
                            break;
                        }
                        response = gson.toJson(manager.getListAllTasks());
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic$", path)) {
                        rCode = 200;
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            response = gson.toJson(manager.getEpicById(id));
                            break;
                        }
                        response = gson.toJson(manager.getListAllEpics());
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        rCode = 200;
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            response = gson.toJson(manager.getSubtaskById(id));
                            break;
                        }
                        response = gson.toJson(manager.getListAllSubtasks());
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String idStr = path.replaceFirst("/tasks/task/", "");
                        int id  = Integer.parseInt(idStr);
                        response = gson.toJson(manager.getTaskById(id));
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                        String idStr = path.replaceFirst("/tasks/epic/", "");
                        int id  = Integer.parseInt(idStr);
                        response = gson.toJson(manager.getEpicById(id));
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                        String idStr = path.replaceFirst("/tasks/subtask/", "");
                        int id  = Integer.parseInt(idStr);
                        response = gson.toJson(manager.getSubtaskById(id));
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks$", path)) {
                        response = gson.toJson(manager.getAllTasksAllTypes());
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks/history$", path)) {
                        response = gson.toJson(manager.getHistory());
                        rCode = 200;
                        break;
                    }
                case "POST":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        rCode = 201;
                        String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(body, Task.class);
                        task.setId(task.getCountTaskId() + 1);
                        Task.setCountTaskId(task.getCountTaskId() + 1);
                        task.setStatus(TaskStatuses.NEW);
                        manager.createTask(task);
                        response = gson.toJson(task);
                        break;
                    }
                    break;
                case "DELETE":
                    //
                    break;
                default:
                    //
            }

            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static int getIdFromQuery(String[] params) {
        return Arrays.stream(params)
                .filter(i -> i.matches("^id=\\d+"))
                .map(i -> i.split("="))
                .mapToInt(i -> Integer.parseInt(i[1]))
                .findAny().getAsInt();
    }

    static {
        Task task1 = new Task("task1", "desc", "12.12.2323 12:00", 60);
        Task task2 = new Task("task2", "desc", "12.12.2323 13:00", 60);
        Epic epic1 = new Epic("epic1", "desc");
        Subtask subtask1 = new Subtask("subtask1", "desc", epic1, "12.12.2323 14:00", 60);
        Subtask subtask2 = new Subtask("subtask2", "desc", epic1, "12.12.2323 15:00", 60);
        Subtask subtask3 = new Subtask("subtask3", "desc", epic1, "12.12.2323 16:00", 60);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);


        manager.getSubtaskById(subtask3.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
    }
}
