import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> mapOfTasksAllTypes = new HashMap<>();

    public ArrayList<String> getListAllTasks() {
        ArrayList<String> listOfTasks = new ArrayList<>();
        for (Task value : mapOfTasksAllTypes.values()) {
            listOfTasks.add(value.name);
        }
        return listOfTasks;
    }

    public void deleteAllTasks() {
        mapOfTasksAllTypes.clear();
    }

    public Task getTaskById(int id) {
        if (mapOfTasksAllTypes.containsKey(id)) {
            return mapOfTasksAllTypes.get(id);
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
            return null; // изменить логику, либо обработать этот null
        }
    }

    public void createTask(Task task) {
        mapOfTasksAllTypes.put(task.id, task);
    }

    public void updateTask(int id, String name, String description) {
        Task task = new Task(id, name, description);
        task.status = mapOfTasksAllTypes.get(id).status;
        mapOfTasksAllTypes.put(id, task);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "allTasksAllTypes=" + mapOfTasksAllTypes +
                "}";
    }
}
