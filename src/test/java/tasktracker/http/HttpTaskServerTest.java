package tasktracker.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {

    private static final String URL = "http://localhost:8080";
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    TaskManager manager;
    Task task;
    Epic epic;
    Subtask subtask;
    Gson gson = Managers.getGson();

    @BeforeEach
    void setUp() throws IOException {
        // стар серверов и инициализация менеджеров
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
        // создание первичных задач
        task = new Task("task1", "desc", "12.12.2323 12:00", 60);
        epic = new Epic("epic1", "desc");
        subtask = new Subtask("subtask1", "desc", epic, "12.12.2323 14:00", 60);
        Epic epic2 = new Epic("epic2", "desc");
        // заполнение менеджера задачами
        httpTaskServer.getManager().createTask(task);
        httpTaskServer.getManager().createEpic(epic);
        httpTaskServer.getManager().createSubtask(subtask);
        httpTaskServer.getManager().createEpic(epic2);
        // заполняем историю
        httpTaskServer.getManager().getTaskById(task.getId());
        httpTaskServer.getManager().getEpicById(epic.getId());
        httpTaskServer.getManager().getSubtaskById(subtask.getId());
    }

    @AfterEach
    void tearDown() {
        Task.setCountTaskId(0);
        httpTaskServer.stop(0);
        kvServer.stop(0);
    }

    @Test
    void shouldGetAllTasksAllTypes() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks");
        String expected = gson.toJson(manager.getAllTasksAllTypes());
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetHistory() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/history");
        String expected = gson.toJson(manager.getHistory());
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTasks() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/task");
        String expected = gson.toJson(manager.getListAllTasks());
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTaskById() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/task?id=1");
        String expected = gson.toJson(manager.getTaskById(1));
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetEpics() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/epic");
        String expected = gson.toJson(manager.getListAllEpics());
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetEpicById() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/epic?id=2");
        String expected = gson.toJson(manager.getEpicById(2));
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetSubtasks() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/subtask");
        String expected = gson.toJson(manager.getListAllSubtasks());
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetSubtaskById() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/subtask?id=3");
        String expected = gson.toJson(manager.getSubtaskById(3));
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetSubtasksFromEpic() {
        HttpResponse<String> response = getResponseForGETEndpoints("/tasks/subtask/epic?id=2");
        String expected = gson.toJson(manager.getEpicById(2).getEpicSubtasks());
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateTask() {
        // По идее в API задачи будут приходить в таком виде, поэтому отправляю просто body в формате json
        String body = "{\"name\":\"new task for create\",\"description\":\"desc\",\"status\":\"NEW\"," +
                "\"startTime\":\"12.12.2323 16:00\",\"duration\":60}";
        HttpResponse<String> response = getResponseForPOSTEndpoints("/tasks/task", body);

        String expected = "{\"id\":5,\"name\":\"new task for create\",\"description\":\"desc\",\"status\":\"NEW\"," +
                "\"startTime\":\"12.12.2323 16:00\",\"duration\":60}";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateTask() {
        String body = "{\"id\":1,\"name\":\"updated task\",\"description\":\"this task is done\",\"status\":\"DONE\"," +
                "\"startTime\":\"12.12.2323 12:00\",\"duration\":60}";
        HttpResponse<String> response = getResponseForPOSTEndpoints("/tasks/task?id=1", body);

        String expected = "{\"id\":1,\"name\":\"updated task\",\"description\":\"this task is done\",\"status\":\"DONE\"," +
                "\"startTime\":\"12.12.2323 12:00\",\"duration\":60}";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateEpic() {
        String body = "{\"name\":\"new epic for create\",\"description\":\"desc\"}";
        HttpResponse<String> response = getResponseForPOSTEndpoints("/tasks/epic", body);

        String expected = "{\"epicSubtasks\":{},\"endTime\":null,\"id\":5,\"name\":\"new epic for create\"," +
                "\"description\":\"desc\",\"status\":\"NEW\",\"startTime\":null,\"duration\":0}";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateEpic() {
        String body = "{\"name\":\"updated epic\",\"description\":\"only name and desc can be updated\"}";
        HttpResponse<String> response = getResponseForPOSTEndpoints("/tasks/epic?id=4", body);

        String expected = "{\"epicSubtasks\":{},\"endTime\":null,\"id\":4,\"name\":\"updated epic\"," +
            "\"description\":\"only name and desc can be updated\",\"status\":\"NEW\",\"startTime\":null,\"duration\":0}";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateSubtask() {
        String body = "{\"name\":\"new subtask\",\"description\":\"desc\",\"epicId\":4,\"status\":\"NEW\"," +
                "\"startTime\":\"12.12.2323 21:00\",\"duration\":60}";
        HttpResponse<String> response = getResponseForPOSTEndpoints("/tasks/subtask", body);

        String expected = "{\"epicName\":\"epic2\",\"epicId\":4,\"id\":5,\"name\":\"new subtask\"," +
                "\"description\":\"desc\",\"status\":\"NEW\",\"startTime\":\"12.12.2323 21:00\",\"duration\":60}";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateSubtask() {
        String body = "{\"name\":\"updated subtask\",\"description\":\"desc\",\"epicId\":2,\"status\":\"DONE\"," +
                "\"startTime\":\"12.12.2323 14:00\",\"duration\":60}";
        HttpResponse<String> response = getResponseForPOSTEndpoints("/tasks/subtask?id=3", body);

        String expected = "{\"epicName\":\"epic1\",\"epicId\":2,\"id\":3,\"name\":\"updated subtask\"," +
                "\"description\":\"desc\",\"status\":\"DONE\",\"startTime\":\"12.12.2323 14:00\",\"duration\":60}";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteTasks() {
        HttpResponse<String> response = getResponseForDELETEEndpoints("/tasks/task");
        String expected = "Все задачи удалены";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteTaskById() {
        HttpResponse<String> response = getResponseForDELETEEndpoints("/tasks/task?id=1");
        String expected = "Задача id = 1 удалена";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteEpics() {
        HttpResponse<String> response = getResponseForDELETEEndpoints("/tasks/epic");
        String expected = "Все эпики удалены";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteEpicById() {
        HttpResponse<String> response = getResponseForDELETEEndpoints("/tasks/epic?id=4");
        String expected = "Эпик id = 4 удален";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteSubtasks() {
        HttpResponse<String> response = getResponseForDELETEEndpoints("/tasks/subtask");
        String expected = "Все подзадачи удалены";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteSubtaskById() {
        HttpResponse<String> response = getResponseForDELETEEndpoints("/tasks/subtask?id=3");
        String expected = "Подзадача id = 3 удалена";
        String actual = response.body();
        assertEquals(expected, actual);
    }

    HttpResponse<String> getResponseForGETEndpoints(String path) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + path))
                .GET()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    HttpResponse<String> getResponseForPOSTEndpoints(String path, String body) {
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    HttpResponse<String> getResponseForDELETEEndpoints(String path) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + path))
                .DELETE()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}