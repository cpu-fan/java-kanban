package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final File FILE_PATH = new File("src/main/resources/fileForSave.csv");

    public static void main(String[] args) {
//        System.out.println(LocalDateTime.now());
//        Task task = new Task("name", "desc", "21.08.2022 15:00", 30);
//        Epic epic = new Epic("epic name", "epic desc");
//        Subtask subtask = new Subtask("subtask name", "subtask desc", epic, "21.08.2022 10:00", 60);
//        System.out.println(task.getEndTime());
//        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("task1 name", "task1 desc", "22.08.2022 18:00", 30);
        Task task2 = new Task("task2 name", "task2 desc", "22.08.2022 15:00", 30);
        Task task3 = new Task("task3 name", "task3 desc", "22.08.2022 18:00", 30);
        Epic epic1 = new Epic("epic name", "epic desc");
        Subtask subtask1 = new Subtask("subtask1", "desc subtask1", epic1, "22.08.2022 15:00", 60);
        Subtask subtask2 = new Subtask("subtask2", "desc subtask2", epic1, "22.08.2022 16:00", 60);
        Subtask subtask3 = new Subtask("subtask3", "desc subtask3", epic1, "22.08.2022 17:00", 60);

        TaskManager manager = Managers.getDefault();
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
    }
}
