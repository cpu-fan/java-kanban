package tasktracker.tasks;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatuses status;
    protected String epic = "";
    protected static int countTaskId;

    // Конструктор для новых задач со счетчиком для id.
    public Task(String name, String description) {
        id = ++countTaskId;
        this.name = name;
        this.description = description;
        this.status = TaskStatuses.NEW; // все новые задачи создаются по умолчанию со статусом NEW
    }

    // Конструктор для обновления задач без счетчика и с обновлением статуса.
    public Task(int id, String name, String description, TaskStatuses status) {
        this(name, description);
        this.id = id;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static String toString(Task task) {
        String taskType = String.valueOf(TaskTypes.TASK);
        if (task.getClass().equals(Epic.class)) {
            taskType = String.valueOf(TaskTypes.EPIC);
        } else {
            taskType = String.valueOf(TaskTypes.SUBTASK);
        }
        return task.id + "," + taskType + "," + task.name + ","
                + task.status + "," + task.description + "," + task.epic;
    }
}
