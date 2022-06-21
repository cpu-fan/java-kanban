package project.manager;

import project.tasks.Epic;
import project.tasks.Subtask;
import project.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

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
