package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
//        System.out.println(LocalDateTime.now());
//        Task task = new Task("name", "desc", "21.08.2022 15:00", 30);
//        Epic epic = new Epic("epic name", "epic desc");
//        Subtask subtask = new Subtask("subtask name", "subtask desc", epic, "21.08.2022 10:00", 60);
//        System.out.println(task.getEndTime());
//        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("task1 name", "task1 desc", "24.08.2022 18:00", 30);
        Task task4 = new Task("task4 name", "task4 desc");
        Task task2 = new Task("task2 name", "task2 desc", "24.08.2022 15:00", 30);
        Task task3 = new Task("task3 name", "task3 desc", "24.08.2022 18:00", 30);
        Epic epic1 = new Epic("epic name", "epic desc");
//        Subtask subtask5 = new Subtask("subtask5", "desc subtask5", epic1, "24.08.2022 20:00", 60);
        Subtask subtask1 = new Subtask("subtask1", "desc subtask1", epic1);
//        Subtask subtask2 = new Subtask("subtask2", "desc subtask2", epic1, "24.08.2022 19:00", 60);
//        Subtask subtask3 = new Subtask("subtask3", "desc subtask3", epic1, "24.08.2022 17:00", 60);
        Subtask subtask4 = new Subtask("subtask4", "desc subtask4", epic1);

        TaskManager manager = Managers.getDefaultFile();
        manager.createTask(task1);
        manager.createTask(task4);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
//        manager.createSubtask(subtask2);
//        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);
//        manager.createSubtask(subtask5);
        // добавлены новые поля в файл и вроде даже как записываются. надо еще проверить как считываются
        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(Managers.getFilePath());
    }
}
