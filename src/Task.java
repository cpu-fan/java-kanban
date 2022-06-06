public class Task {
    protected String name;
    protected String description;
    protected String status;
    protected int id;
    protected static int count;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        count++;
        id = count;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
