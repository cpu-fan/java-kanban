package tasktracker.main;

import tasktracker.managers.Managers;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static final File FILE_PATH = new File("src/tasktracker/taskmanager/files/fileForSave.csv");

    public static void main(String[] args) {

        // Проверьте работу сохранения и восстановления менеджера из файла (сериализацию).
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(FILE_PATH);

        // 1. Заведите несколько разных задач, эпиков и подзадач.
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

        // 2. Запросите некоторые из них, чтобы заполнилась история просмотра.
        fileBackedTaskManager.getTaskById(task02.getId());
        fileBackedTaskManager.getEpicById(epic01.getId());
        fileBackedTaskManager.getSubtaskById(subtask01.getId());

        // 3. Создайте новый FileBackedTasksManager менеджер из этого же файла.
        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(FILE_PATH);

        // 4. Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи, которые были
        // в старом, есть в новом менеджере - проверил через дебаггер, вроде все ок =))
    }
}
