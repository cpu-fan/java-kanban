package project.manager;

import project.tasks.Epic;
import project.tasks.Subtask;
import project.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    // Хранение каждого типа задачи в отдельной коллекции
    private HashMap<Integer, Task> mapOfTasks = new HashMap<>();
    private HashMap<Integer, Epic> mapOfEpics = new HashMap<>();
    private HashMap<Integer, Subtask> mapOfSubtasks = new HashMap<>();

    // Методы для помещения созданной задачи в коллекцию своего типа
    public void createTask(Task task) {
        mapOfTasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        mapOfEpics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        mapOfSubtasks.put(subtask.getId(), subtask);
    }

    // Методы для получения задачи по ее идентификатору из соответствующей коллекции
    public Task getTaskById(int id) {
        if (mapOfTasks.containsKey(id)) {
            return mapOfTasks.get(id);
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
            return null;
        }
    }

    public Epic getEpicById(int id) {
        if (mapOfEpics.containsKey(id)) {
            return mapOfEpics.get(id);
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
            return null;
        }
    }

    public Subtask getSubtaskById(int id) {
        if (mapOfSubtasks.containsKey(id)) {
            return mapOfSubtasks.get(id);
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
            return null;
        }
    }

    // Методы для получения списка всех задач из соответствующей коллекции
    public ArrayList<Task> getListAllTasks() {
        return new ArrayList<>(mapOfTasks.values());
    }

    public ArrayList<Epic> getListAllEpics() {
        return new ArrayList<>(mapOfEpics.values());
    }

    public ArrayList<Subtask> getListAllSubtasks() {
        return new ArrayList<>(mapOfSubtasks.values());
    }

    // Методы для обновления задач соответствующей коллекции
    public void updateTask(Task task) {
        mapOfTasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        mapOfEpics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        mapOfSubtasks.put(subtask.getId(), subtask);
    }

    // Методы для удаления задачи по идентификатору соответствующей коллекции
    public void removeTaskById(Task task) {
        if (mapOfTasks.containsKey(task.getId())) {
            mapOfTasks.remove(task.getId());
        } else {
            System.out.println("Задачи с таким идентификатором не существует!");
        }
    }

    public void removeEpicById(Epic epic) {
        if (mapOfEpics.containsKey(epic.getId())) {
            epic.clearSubtask(); // очищаем эпик от подзадач
            mapOfEpics.remove(epic.getId()); // удаляем сам эпик из таблицы эпиков
        } else {
            System.out.println("Эпика с таким идентификатором не существует!");
        }
    }

    public void removeSubtaskById(Subtask subtask) {
        if (mapOfSubtasks.containsKey(subtask.getId())) {
            int epicId = mapOfSubtasks.get(subtask.getId()).getEpicId(); // получаем id эпика, в котором содержится подзадача
            mapOfEpics.get(epicId).deleteSubtask(subtask.getId()); // удаляем эту подзадачу в ее эпике и пересчитываем статус эпика
            mapOfSubtasks.remove(subtask.getId()); // удаляем саму подзадачу
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует!");
        }
    }

    // Методы для удаления всех задач в соответствующей коллекции
    public void deleteAllTasks() {
        mapOfTasks.clear();
    }

    public void deleteAllEpics() {
        mapOfEpics.clear();
        mapOfSubtasks.clear();
    }

    public void deleteAllSubtasks() {
        mapOfSubtasks.clear();
        // также очищаем подзадачи в эпиках
        for (Epic epic : mapOfEpics.values()) {
            epic.getEpicSubtasks().clear();
        }
    }

    // Метод для получения списка всех подзадач определённого эпика.
    public HashMap<Integer, Subtask> getListAllSubtasksByEpic(Epic epic) {
        return epic.getEpicSubtasks();
    }

    @Override
    public String toString() {
        return "Manager{" +
                "mapOfTasks=" + mapOfTasks +
                ", mapOfEpics=" + mapOfEpics +
                ", mapOfSubtasks=" + mapOfSubtasks +
                '}';
    }
}
