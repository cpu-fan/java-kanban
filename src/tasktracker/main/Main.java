package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        // Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = new Task("task1", "desc for task1");
        Task task2 = new Task("task2", "desc for task2");
        Epic epic1 = new Epic("epic1", "desc for epic1");
        Epic epic2 = new Epic("epic2", "desc for epic2");
        Subtask subtask1 = new Subtask("subtask1", "desc for subtask1", epic1);
        Subtask subtask2 = new Subtask("subtask2", "desc for subtask2", epic1);
        Subtask subtask3 = new Subtask("subtask3", "desc for subtask3", epic2);

        // помещение задач в свои коллекции для хранения
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        // Cоздайте несколько задач разного типа и вызовите разные методы интерфейса TaskManager и напечатайте историю
        // просмотров после каждого вызова. Если код рабочий, то история просмотров задач будет отображаться корректно.
        taskManager.getTaskById(task2.getId());
        System.out.println(taskManager.getHistoryList());

        taskManager.getSubtaskById(subtask3.getId());
        System.out.println(taskManager.getHistoryList());

        taskManager.getEpicById(epic1.getId());
        System.out.println(taskManager.getHistoryList());
    }
}
