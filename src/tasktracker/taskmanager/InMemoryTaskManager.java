package tasktracker.taskmanager;

import tasktracker.historymanager.HistoryManager;
import tasktracker.managers.Managers;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    // Хранение истории просмотра задач
    private final HistoryManager historyList = Managers.getDefaultHistory();

    // Хранение каждого типа задачи в отдельной коллекции
    private final HashMap<Integer, Task> mapOfTasks = new HashMap<>();
    private final HashMap<Integer, Epic> mapOfEpics = new HashMap<>();
    private final HashMap<Integer, Subtask> mapOfSubtasks = new HashMap<>();

    // Методы для помещения созданной задачи в коллекцию своего типа
    @Override
    public void createTask(Task task) {
        mapOfTasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        mapOfEpics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        mapOfSubtasks.put(subtask.getId(), subtask);
    }

    // Методы для получения задачи по ее идентификатору из соответствующей коллекции
    @Override
    public Task getTaskById(int id) {
        if (mapOfTasks.containsKey(id)) {
            historyList.addToHistory(mapOfTasks.get(id));
            return mapOfTasks.get(id);
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (mapOfEpics.containsKey(id)) {
            historyList.addToHistory(mapOfEpics.get(id));
            return mapOfEpics.get(id);
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (mapOfSubtasks.containsKey(id)) {
            historyList.addToHistory(mapOfSubtasks.get(id));
            return mapOfSubtasks.get(id);
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
            return null;
        }
    }

    // Методы для получения списка всех задач из соответствующей коллекции
    @Override
    public ArrayList<Task> getListAllTasks() {
        return new ArrayList<>(mapOfTasks.values());
    }

    @Override
    public ArrayList<Epic> getListAllEpics() {
        return new ArrayList<>(mapOfEpics.values());
    }

    @Override
    public ArrayList<Subtask> getListAllSubtasks() {
        return new ArrayList<>(mapOfSubtasks.values());
    }

    // Методы для обновления задач соответствующей коллекции
    @Override
    public void updateTask(Task task) {
        mapOfTasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        mapOfEpics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int oldEpicId = mapOfSubtasks.get(subtask.getId()).getEpicId(); // получаем и сохраняем старый эпик
        boolean isNewEpic = subtask.getEpicId() != oldEpicId; // проверка на новый эпик
        if (isNewEpic) {
            mapOfEpics.get(oldEpicId).deleteSubtask(subtask.getId()); // удаление подзадачи из старого эпика
        }
        mapOfEpics.get(subtask.getEpicId()).addSubtask(subtask); // обновляем подзадачу в ее эпике и пересчитываем статус
        mapOfSubtasks.put(subtask.getId(), subtask);
    }

    // Методы для удаления задачи по идентификатору соответствующей коллекции
    @Override
    public void removeTaskById(int taskId) {
        if (mapOfTasks.containsKey(taskId)) {
            mapOfTasks.remove(taskId);
        } else {
            System.out.println("Задачи с таким идентификатором не существует!");
        }
    }

    @Override
    public void removeEpicById(int epicId) {
        if (mapOfEpics.containsKey(epicId)) {
            mapOfEpics.remove(epicId); // удаляем сам эпик из таблицы эпиков

            // удаляем подзадачи связанные с этим эпиком со списка подзадач
            ArrayList<Integer> subtaskIds = new ArrayList<>(); // отдельный список для id подзадач удаленного эпика
            for (Subtask value : mapOfSubtasks.values()) {
                if (value.getEpicId() == epicId) {
                    subtaskIds.add(value.getId());
                }
            }

            for (Integer subtaskId : subtaskIds) {
                mapOfSubtasks.remove(subtaskId); // проходимся по списку подзадач и удаляем собранные id на строке 107
            }
            /* Примечание: эти приседания с дополнительным списком ArrayList<Integer> subtaskIds для id подзадач
            * удаленного эпика сделаны, чтобы избежать ошибки ConcurrentModificationException, которая возникает когда
            * я сразу пытаюсь использовать на 107 строке конструкцию mapOfSubtasks.remove(value.getId()); */
        } else {
            System.out.println("Эпика с таким идентификатором не существует!");
        }
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        if (mapOfSubtasks.containsKey(subtaskId)) {
            int epicId = mapOfSubtasks.get(subtaskId).getEpicId(); // получаем id эпика, в котором содержится подзадача
            mapOfEpics.get(epicId).deleteSubtask(subtaskId); // удаляем эту подзадачу в ее эпике и пересчитываем статус эпика
            mapOfSubtasks.remove(subtaskId); // удаляем саму подзадачу
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует!");
        }
    }

    // Методы для удаления всех задач в соответствующей коллекции
    @Override
    public void deleteAllTasks() {
        mapOfTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        mapOfEpics.clear();
        mapOfSubtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        mapOfSubtasks.clear();
        // также очищаем подзадачи в эпиках
        for (Epic epic : mapOfEpics.values()) {
            epic.getEpicSubtasks().clear();
        }
    }

    // Метод для получения списка всех подзадач определённого эпика.
    @Override
    public HashMap<Integer, Subtask> getListAllSubtasksByEpic(Epic epic) {
        return epic.getEpicSubtasks();
    }

    // Метод для получения списка истории просмотренных задач
    @Override
    public List<Task> getHistoryList() {
        return historyList.getHistory();
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
