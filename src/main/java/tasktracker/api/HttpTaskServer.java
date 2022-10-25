package tasktracker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();
    private static TaskManager manager = Managers.getDefaultFile();

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServerHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

        // потом удалить
        Scanner scanner = new Scanner(System.in);
        System.out.println("To stop the server enter \"stop\"");
        String stopSignal = scanner.next();
        if (stopSignal.equals("stop") || stopSignal.equals("ыещз")) {
            httpServer.stop(0);
            System.out.println("Server is stopped");
        }
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
                            if (response.equals("null")) {
                                response = "Задачи с таким id не существует";
                                rCode = 404;
                            }
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
                            if (response.equals("null")) {
                                response = "Эпика с таким id не существует";
                                rCode = 404;
                            }
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
                            if (response.equals("null")) {
                                response = "Подзадачи с таким id не существует";
                                rCode = 404;
                            }
                            break;
                        }
                        response = gson.toJson(manager.getListAllSubtasks());
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask/epic$", path) && query != null && query.contains("id")) {
                        String[] params = query.split("&");
                        int id = getIdFromQuery(params);
                        HashMap<Integer, Subtask> temp = null;
                        try {
                            temp = manager.getEpicById(id).getEpicSubtasks();
                        } catch (NullPointerException e) {
                            response = "Эпика с таким id не существует";
                            rCode = 404;
                            break;
                        }
                        response = gson.toJson(temp);
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
                    // Добавление и обновление задач
                    if (Pattern.matches("^/tasks/task$", path)) {
                        rCode = 201;
                        String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

                        Task task = gson.fromJson(body, Task.class);
                        // если приходит в POST запросе параметр id, то необходимо обновление таски
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            task.setId(id);
                            manager.updateTask(task);
                            response = gson.toJson(manager.getTaskById(id));
                            break;
                        }
                        task = new Task(task.getName(), task.getDescription(),
                                task.getStartTimeInFormat(), (int) task.getDuration());
                        manager.createTask(task);

                        response = gson.toJson(task);
                        break;
                    }

                    // Добавление и обновление эпиков
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        rCode = 201;
                        String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

                        Epic epic = gson.fromJson(body, Epic.class);
                        // если приходит в POST запросе параметр id, то значит необходимо обновление эпика
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            // У эпиков можно самим обновлять только имя и описание, т.к. остальное рассчитывается
                            String newEpicName = epic.getName();
                            String oldEpicName = manager.getEpicById(id).getName();
                            String newEpicDesc = epic.getDescription();
                            String oldEpicDesc = manager.getEpicById(id).getDescription();
                            if (!newEpicName.equals(oldEpicName) || !newEpicDesc.equals(oldEpicDesc)) {
                                manager.getEpicById(id).setName(newEpicName);
                                manager.getEpicById(id).setDescription(newEpicDesc);
                            }
                            response = gson.toJson(manager.getEpicById(id));
                            rCode = 200;
                            if (response.equals("null")) {
                                response = "Эпика с таким id не существует";
                                rCode = 404;
                            }
                            break;
                        }
                        epic = new Epic(epic.getName(), epic.getDescription());
                        manager.createEpic(epic);

                        response = gson.toJson(epic);
                        break;
                    }

                    // Добавление и обновление сабтасок
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        rCode = 201;
                        String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        // если приходит в POST запросе параметр id, то необходимо обновление сабтаски
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            subtask.setId(id);
                            manager.updateSubtask(subtask);
                            response = gson.toJson(manager.getSubtaskById(id));
                            break;
                        }
                        subtask = new Subtask(subtask.getName(), subtask.getDescription(),
                                manager.getEpicById(subtask.getEpicId()), subtask.getStartTimeInFormat(),
                                (int) subtask.getDuration());
                        manager.createSubtask(subtask);

                        response = gson.toJson(subtask);
                        break;
                    }

                case "DELETE":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            if (manager.getTaskById(id) == null) {
                                response = "Задачи с таким id не существует";
                                rCode = 404;
                                break;
                            }
                            manager.removeTaskById(id);
                            response = "Задача id = " + id + " удалена";
                            rCode = 200;
                            break;
                        }
                        manager.deleteAllTasks();
                        response = "Все задачи удалены";
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic$", path)) {
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            if (manager.getEpicById(id) == null) {
                                response = "Эпика с таким id не существует";
                                rCode = 404;
                                break;
                            }
                            manager.removeEpicById(id);
                            response = "Эпик id = " + id + " удален";
                            rCode = 200;
                            break;
                        }
                        manager.deleteAllEpics();
                        response = "Все эпики удалены";
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        if (query != null && query.contains("id")) {
                            String[] params = query.split("&");
                            int id = getIdFromQuery(params);
                            if (manager.getSubtaskById(id) == null) {
                                response = "Подзадачи с таким id не существует";
                                rCode = 404;
                                break;
                            }
                            manager.removeSubtaskById(id);
                            response = "Подзадача id = " + id + " удалена";
                            rCode = 200;
                            break;
                        }
                        manager.deleteAllSubtasks();
                        response = "Все подзадачи удалены";
                        rCode = 200;
                        break;
                    }
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
        // Будет содержать в startTime null, т.к. не содержит подзадач, чтобы время рассчиталось:
        Epic epic2 = new Epic("epic2", "desc");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createEpic(epic2);

        manager.getSubtaskById(subtask3.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
    }
}
