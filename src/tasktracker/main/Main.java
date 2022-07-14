package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "desc for task1");
        Task task2 = new Task("task2", "desc for task2");
        Task task3 = new Task("task3", "desc for task3");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(task3.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(task1.getId());
        System.out.println(taskManager.getHistory());
    }
}
