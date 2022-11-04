package tasktracker.main;

import org.junit.jupiter.api.Test;
import tasktracker.http.HttpTaskServer;
import tasktracker.http.KVServer;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {
    public static TaskManager httpTaskManagerFrom;
    public static TaskManager httpTaskManagerTo;
    public static void main(String[] args) throws IOException {
        new KVServer().start();

        httpTaskManagerFrom = Managers.getDefault();
        initTasks();

        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManagerFrom);
        httpTaskServer.start();
    }

    public static void initTasks() {
        Task task1 = new Task("task1", "desc", "12.12.2323 13:00", 60);
        Task task2 = new Task("task2", "desc", "12.12.2323 16:00", 60);
        Epic epic1 = new Epic("epic1", "desc");
        Subtask subtask1 = new Subtask("subtask1", "desc", epic1, "12.12.2323 15:00", 60);
        Subtask subtask2 = new Subtask("subtask2", "desc", epic1, "12.12.2323 17:00", 60);
        Subtask subtask3 = new Subtask("subtask3", "desc", epic1, "12.12.2323 14:00", 60);
        Epic epic2 = new Epic("epic2", "desc");

        httpTaskManagerFrom.createTask(task1);
        httpTaskManagerFrom.createTask(task2);
        httpTaskManagerFrom.createEpic(epic1);
        httpTaskManagerFrom.createSubtask(subtask1);
        httpTaskManagerFrom.createSubtask(subtask2);
        httpTaskManagerFrom.createSubtask(subtask3);
        httpTaskManagerFrom.createEpic(epic2);

        httpTaskManagerFrom.getEpicById(epic2.getId());
        httpTaskManagerFrom.getTaskById(task1.getId());
        httpTaskManagerFrom.getSubtaskById(subtask2.getId());
        httpTaskManagerFrom.getEpicById(epic1.getId());
    }
}
