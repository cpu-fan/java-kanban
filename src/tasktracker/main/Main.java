package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // 1. создайте две задачи, эпик с тремя подзадачами и эпик без подзадач
        Task task1 = new Task("task1", "desc for task1");
        Task task2 = new Task("task2", "desc for task2");

        Epic epic1 = new Epic("epic1", "desc for epic1");
        Subtask subtask1 = new Subtask("subtask1 on epic 1", "desc for subtask1", epic1);
        Subtask subtask2 = new Subtask("subtask2 on epic 1", "desc for subtask2", epic1);
        Subtask subtask3 = new Subtask("subtask3 on epic 1", "desc for subtask3", epic1);

        Epic epic2 = new Epic("epic2", "desc for epic2");

        // помещение созданных задач в коллекцию
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.createEpic(epic2);

        // 2. запросите созданные задачи несколько раз в разном порядке и
        // 3. после каждого запроса выведите историю и убедитесь, что в ней нет повторов
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(task1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epic1.getId());
        System.out.println(taskManager.getHistory());

        // 4. удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться
        taskManager.removeTaskById(task2.getId());
        System.out.println(taskManager.getHistory());

        // 5. удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи
        taskManager.removeEpicById(epic1.getId());
        System.out.println(taskManager.getHistory());
    }
}
