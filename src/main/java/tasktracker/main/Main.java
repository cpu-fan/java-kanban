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
        TaskManager manager = Managers.getDefault();

        // 1. Проверка исправления по замечанию в классе Epic (3 ревью)
//        Epic epic = new Epic("epic", "desc");
//        Subtask subtask1 = new Subtask("subtask1", "desc", epic, "11.11.2022 00:00", 60);
//        Subtask subtask2 = new Subtask("subtask2", "desc", epic, "11.11.2022 22:00", 60);
//
//        manager.createEpic(epic);
//        manager.createSubtask(subtask1);
//        manager.createSubtask(subtask2);
//
//        LocalDateTime egst = epic.getStartTime();
//        LocalDateTime eget = epic.getEndTime();
//        long egd = epic.getDuration();

        // 2. Проверка, что не сломалось исправление замечания из первого ревью про 10 тасок
//        Epic epic = new Epic("name", "desc");
//        Subtask subtask3 = new Subtask("name", "desc", epic, "01.12.2022 03:00", 60);
//        Subtask subtask2 = new Subtask("name", "desc", epic, "01.12.2022 02:00", 60);
//        Subtask subtask8 = new Subtask("name", "desc", epic, "01.12.2022 08:00", 60);
//        Subtask subtask4 = new Subtask("name", "desc", epic, "01.12.2022 04:00", 60);
//        Subtask subtask1 = new Subtask("name", "desc", epic, "01.12.2022 01:00", 60);
//        Subtask subtask9 = new Subtask("name", "desc", epic, "01.12.2022 09:00", 60);
//        Subtask subtask5 = new Subtask("name", "desc", epic, "01.12.2022 05:00", 60);
//        Subtask subtask6 = new Subtask("name", "desc", epic, "01.12.2022 06:00", 60);
//        Subtask subtask7 = new Subtask("name", "desc", epic, "01.12.2022 07:00", 60);
//        Subtask subtask10 = new Subtask("name", "desc", epic);

//        manager.createEpic(epic);
//        manager.createSubtask(subtask1);
//        manager.createSubtask(subtask2);
//        manager.createSubtask(subtask3);
//        manager.createSubtask(subtask4);
//        manager.createSubtask(subtask5);
//        manager.createSubtask(subtask6);
//        manager.createSubtask(subtask7);
//        manager.createSubtask(subtask8);
//        manager.createSubtask(subtask9);
//        manager.createSubtask(subtask10);
//        LocalDateTime epicEndTimeCheck = epic.getEndTime();

        // 3.
        Task priTask = new Task("priTask", "priTask desc", "11.11.2222 13:00", 60);
        Task task = new Task("task", "task desc", "11.11.2222 00:00", 60);
        manager.createTask(priTask);
        manager.createTask(task);
    }
}
