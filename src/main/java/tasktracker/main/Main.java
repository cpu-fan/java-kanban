package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    private static final File FILE_PATH = new File("src/main/resources/fileForSave.csv");

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        Task task = new Task("name", "desc", "20.08.2022 15:00", 30);
        System.out.println(task.getEndTime());
        TaskManager manager = Managers.getDefault();
    }
}
