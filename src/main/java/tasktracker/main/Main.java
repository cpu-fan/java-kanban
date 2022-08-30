package tasktracker.main;

import tasktracker.exceptions.TaskTimeValidationException;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        // 1. Проверка исправления по замечанию в классе Epic
        Epic epic = new Epic("epic", "desc");
        Subtask subtask1 = new Subtask("subtask1", "desc", epic, "11.11.2900 11:00", 60);
        Subtask subtask2 = new Subtask("subtask2", "desc", epic, "11.11.3000 11:00", 60);

        TaskManager manager = Managers.getDefault();

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        // 2. Проверка по исправлению в классе InMemoryTaskManager, строка 40
//        Task task1 = new Task("task1", "task1", "12.12.2121 12:00", 60);
//        Task task2 = new Task("task2", "task2", "12.12.2121 12:00", 60);
//
//        TaskManager manager = Managers.getDefault();
//
//        manager.createTask(task1);
//        manager.createTask(task2);

        // 3.
    }
}
