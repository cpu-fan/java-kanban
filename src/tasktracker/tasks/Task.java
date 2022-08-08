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

    // ???
//    public void setId(int id) {
//        this.id = id;
//    }

      // 3
//    @Override
//    public String toString() {
//        return "Task{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", status='" + status + '\'' +
//                '}';
//    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s",
                getId(),
                getType(),
                getName(),
                getStatus(),
                getDescription(),
                "");
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

    public String getDescription() {
        return description;
    }

    public TaskStatuses getStatus() {
        return status;
    }

    public int getEpicId() {
        return epicId;
    }

    public static int getCountTaskId() {
        return countTaskId;
    }
// 1
//    public static String toString(Task task) {
//        String taskType; // для изменения из enum в String
//        String epicId; // для изменения из int в String (для Task и Epic присваивается пустое значение - "")
//
//        if (task.getClass().equals(Task.class)) {
//            taskType = String.valueOf(TaskTypes.TASK);
//            epicId = "";
//        } else if (task.getClass().equals(Epic.class)) {
//            taskType = String.valueOf(TaskTypes.EPIC);
//            epicId = "";
//        } else {
//            taskType = String.valueOf(TaskTypes.SUBTASK);
//            epicId = String.valueOf(task.epicId);
//        }
//        return task.id + "," + taskType + "," + task.name + ","
//                + task.status + "," + task.description + "," + epicId;
//    }
}
