import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasksAllTypes = new HashMap<>();

    public Manager(Task task) {
        tasksAllTypes.put(task.id, task);
    }
}
