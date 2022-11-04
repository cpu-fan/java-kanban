package tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private HttpServer httpServer;
    private static TaskManager manager;
    private static Gson gson;

    public HttpTaskServer(TaskManager manager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServerHandler());
        HttpTaskServer.manager = manager;
        gson = Managers.getGson();
    }

    public TaskManager getManager() {
        return manager;
    }

    public void start() {
        httpServer.start();
        System.out.println("HttpTaskServer запущен на " + PORT + " порту!");
    }

    public void stop(int delay) {
        System.out.println("Останавливаем HttpTaskServer на порту " + PORT + " через " + delay + " секунд");
        httpServer.stop(delay);
    }

    private static class HttpTaskServerHandler implements HttpHandler {
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
                        HashMap<Integer, Subtask> temp = manager.getEpicById(id).getEpicSubtasks();
                        if (temp == null) {
                            response = "Эпика с таким id не существует";
                            rCode = 404;
                            break;
                        }
                        response = gson.toJson(temp);
                        rCode = 200;
                        break;
                    }

                    if (Pattern.matches("^/tasks$", path)) {
                        response = gson.toJson(manager.getPrioritizedTasks());
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
                        // Если в POST запросе приходит задача с id != 0, значит она пришла на обновление
                        if (task.getId() != 0) {
                            manager.updateTask(task);
                            response = gson.toJson(manager.getTaskById(task.getId()));
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
                        // Если в POST запросе приходит эпик с id != 0, значит она пришла на обновление
                        if (epic.getId() != 0) {
                            // У эпиков можно самим обновлять только имя и описание, т.к. остальное рассчитывается
                            String newEpicName = epic.getName();
                            String oldEpicName = manager.getEpicById(epic.getId()).getName();
                            String newEpicDesc = epic.getDescription();
                            String oldEpicDesc = manager.getEpicById(epic.getId()).getDescription();
                            if (!newEpicName.equals(oldEpicName) || !newEpicDesc.equals(oldEpicDesc)) {
                                manager.getEpicById(epic.getId()).setName(newEpicName);
                                manager.getEpicById(epic.getId()).setDescription(newEpicDesc);
                            }
                            response = gson.toJson(manager.getEpicById(epic.getId()));
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
                        // Если в POST запросе приходит сабтаска с id != 0, значит она пришла на обновление
                        if (subtask.getId() != 0) {
                            manager.updateSubtask(subtask);
                            response = gson.toJson(manager.getSubtaskById(subtask.getId()));
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
}
