package tasktracker.taskmanager;

import tasktracker.historymanager.HistoryManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    // Метод для возврата списка истории просмотра задач
    HistoryManager getHistoryList();
    /* Насколько здесь, в TaskManager, было корректно добавлять метод getHistoryList() для возврата списка истории?
    * Ведь похожий метод имеется и в HistoryManager. Это считается дублированием кода?
    * В этом моменте немного запутался... */

    // Методы для помещения созданной задачи в коллекцию своего типа
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Subtask subtask);

    // Методы для получения задачи по ее идентификатору из соответствующей коллекции
    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    // Методы для получения списка всех задач из соответствующей коллекции
    ArrayList<Task> getListAllTasks();
    ArrayList<Epic> getListAllEpics();
    ArrayList<Subtask> getListAllSubtasks();

    // Методы для обновления задач соответствующей коллекции
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    // Методы для удаления задачи по идентификатору соответствующей коллекции
    void removeTaskById(int taskId);
    void removeEpicById(int epicId);
    void removeSubtaskById(int subtaskId);

    // Методы для удаления всех задач в соответствующей коллекции
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    // Метод для получения списка всех подзадач определённого эпика.
    HashMap<Integer, Subtask> getListAllSubtasksByEpic(Epic epic);
}
