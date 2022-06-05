import java.util.ArrayList;

public class Task {
    protected String name;
    protected String description;
    protected String status;
    protected int id;
    protected static int count;
    protected ArrayList<Task> listAllTasks;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        count++;
        id = count;
    }
}
