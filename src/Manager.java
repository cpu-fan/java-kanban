import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    // Хранение каждого типа задачи в отдельной коллекции
    HashMap<Integer, Task> mapOfTasks = new HashMap<>();
    HashMap<Integer, Epic> mapOfEpics = new HashMap<>();
    HashMap<Integer, Subtask> mapOfSubtasks = new HashMap<>();

    // Методы для помещения созданной задачи в коллекцию своего типа
    public void createTask(Task task) {
        mapOfTasks.put(task.id, task);
    }

    public void createEpic(Epic epic) {
        mapOfEpics.put(epic.id, epic);
    }

    public void createSubtask(Subtask subtask) {
        mapOfSubtasks.put(subtask.id, subtask);
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
        mapOfTasks.put(task.id, task);
    }

    public void updateEpic(Epic epic) {
        mapOfEpics.put(epic.id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        mapOfSubtasks.put(subtask.id, subtask);
    }

    // Методы для удаления задачи по идентификатору соответствующей коллекции
    public void removeTaskById(int taskId) {
        if (mapOfTasks.containsKey(taskId)) {
            mapOfTasks.remove(taskId);
        } else {
            System.out.println("Задачи с таким идентификатором не существует!");
        }
    }

    public void removeEpicById(int epicId) {
        /* Из-за зависимости эпиков с подзадачами, мне показалось правильнее сделать, если удаление будет происходить
        * только пустых эпик, т.е. предварительно необходимо очищать его от задач отдельно. */
        if (mapOfEpics.containsKey(epicId) && mapOfEpics.get(epicId).epicSubtasks.size() == 0) {
            mapOfEpics.remove(epicId);
        } else if (mapOfEpics.get(epicId).epicSubtasks.size() != 0) {
            System.out.println("Не возможно удалить эпик, в нем есть задачи!");
        } else {
            System.out.println("Эпика с таким идентификатором не существует!");
        }
    }

    public void removeSubtaskById(int subtaskId) {
        if (mapOfSubtasks.containsKey(subtaskId)) {
            int epicId = mapOfSubtasks.get(subtaskId).epicId; // получаем id эпика, в котором содержится подзадача
            mapOfEpics.get(epicId).epicSubtasks.remove(subtaskId); // удаляем эту подзадачу в ее эпике
            mapOfSubtasks.remove(subtaskId); // удаляем саму подзадачу
            mapOfEpics.get(epicId).calculateEpicStatus(); // пересчитываем статус эпика
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует!");
        }
    }

    // Методы для удаления всех задач в соответствующей коллекции
    public void deleteAllTasks() {
        mapOfTasks.clear();
    }

    public void deleteAllEpics() {
        /* Правильнее, как мне кажется, перед удалением эпика проверить содержатся ли в нем задачи и удалять только те,
        * в которых задач нет. Поэтому для начала удаляем все эпики не содержащие задачи. */
        ArrayList<Integer> epicIds = new ArrayList<>(); // список для хранения id эпиков у которых пустые списки задач
        for (Epic epic : mapOfEpics.values()) {
            if (epic.epicSubtasks.size() == 0) {
                epicIds.add(epic.id);
            } else {
                System.out.println("Эпик '" + epic.name + "' содержит задачи. Для начала удалите его подзадачи.");
            }
        }
        // Удаляю эпики у которых список задач пустой.
        // Сохранение в отдельный список сделано, чтобы избежать ConcurrentModificationException.
        for (Integer id : epicIds) {
            mapOfEpics.remove(id);
        }
    }

    public void deleteAllSubtasks() {
        mapOfSubtasks.clear();
        // также очищаем подзадачи в эпиках
        for (Epic epic : mapOfEpics.values()) {
            epic.epicSubtasks.clear();
        }
    }

    // Метод для получения списка всех подзадач определённого эпика.
    public HashMap<Integer, Subtask> getListAllSubtasksByEpic(Epic epic) {
        return epic.epicSubtasks;
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
