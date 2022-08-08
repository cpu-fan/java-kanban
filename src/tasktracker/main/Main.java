package tasktracker.main;

import tasktracker.converter.CsvConverter;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.InMemoryTaskManager;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final String FILE_PATH = "src/tasktracker/taskmanager/files/tasks.csv";

    public static void main(String[] args) {

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(FILE_PATH);

        Task task01 = new Task("task01", "desc for task01");
        Epic epic01 = new Epic("epic01", "desc for epic01");
        Task task02 = new Task("task02", "desc for task02");
        Subtask subtask01 = new Subtask("subtask01", "desc for subtask01", epic01);
        Task task03 = new Task("task03", "desc for task03");
        fileBackedTaskManager.createTask(task01);
        fileBackedTaskManager.createTask(task02);
        fileBackedTaskManager.createTask(task03);
        fileBackedTaskManager.createEpic(epic01);
        fileBackedTaskManager.createSubtask(subtask01);

        task01 = new Task(task01.getId(), "task01 edited", "edited desk", TaskStatuses.IN_PROGRESS);
        fileBackedTaskManager.updateTask(task01);

        fileBackedTaskManager.getTaskById(task03.getId());
        fileBackedTaskManager.getEpicById(epic01.getId());
        fileBackedTaskManager.getTaskById(task01.getId());
        fileBackedTaskManager.getTaskById(task02.getId());
        fileBackedTaskManager.getSubtaskById(subtask01.getId());


//        TaskManager taskManager = Managers.getDefault();
//
//        // 1. создайте две задачи, эпик с тремя подзадачами и эпик без подзадач
//        Task task1 = new Task("task1", "desc for task1");
//        Task task2 = new Task("task2", "desc for task2");
//
//        Epic epic1 = new Epic("epic1", "desc for epic1");
//        Subtask subtask1 = new Subtask("subtask1 on epic 1", "desc for subtask1", epic1);
//        Subtask subtask2 = new Subtask("subtask2 on epic 1", "desc for subtask2", epic1);
//        Subtask subtask3 = new Subtask("subtask3 on epic 1", "desc for subtask3", epic1);
//
//        Epic epic2 = new Epic("epic2", "desc for epic2");
//
//        // помещение созданных задач в коллекцию
//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
//
//        taskManager.createEpic(epic1);
//        taskManager.createSubtask(subtask1);
//        taskManager.createSubtask(subtask2);
//        taskManager.createSubtask(subtask3);
//
//        taskManager.createEpic(epic2);
//
//        // 2. запросите созданные задачи несколько раз в разном порядке и
//        // 3. после каждого запроса выведите историю и убедитесь, что в ней нет повторов
//        taskManager.getSubtaskById(subtask2.getId());
//        taskManager.getEpicById(epic2.getId());
//        taskManager.getTaskById(task1.getId());
//        taskManager.getTaskById(task2.getId());
//        taskManager.getEpicById(epic1.getId());
//        System.out.println(taskManager.getHistory());
//
//        taskManager.getTaskById(task1.getId());
//        System.out.println(taskManager.getHistory());
//        taskManager.getEpicById(epic1.getId());
//        System.out.println(taskManager.getHistory());
//
//        // 4. удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться
//        taskManager.removeTaskById(task2.getId());
//        System.out.println(taskManager.getHistory());
//
//        // 5. удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи
//        taskManager.removeEpicById(epic1.getId());
//        System.out.println(taskManager.getHistory());
    }
}
