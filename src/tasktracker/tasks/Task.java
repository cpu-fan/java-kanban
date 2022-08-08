package tasktracker.tasks;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatuses status;
    protected int epicId;
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

    public int getId() {
        return id;
    }

    public TaskTypes getType() {
        if (this.getClass().equals(Task.class)) {
            return TaskTypes.TASK;
        } else if (this.getClass().equals(Epic.class)) {
            return TaskTypes.EPIC;
        } else {
            return TaskTypes.SUBTASK;
        }
    }

    public String getName() {
        return name;
    }

    public TaskStatuses getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s,",
                getId(),
                getType(),
                getName(),
                getStatus(),
                getDescription());
    }
}
