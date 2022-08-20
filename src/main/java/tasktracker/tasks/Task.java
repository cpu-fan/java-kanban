package tasktracker.tasks;

import tasktracker.exceptions.ManagerSaveException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatuses status;
    protected int epicId;
    protected static int countTaskId;
    protected LocalDateTime startTime;
    protected long duration;

    // Новый конструктор, который возможно придет на замену старому ниже
    public Task(String name, String description, String startTime, int duration) {
        id = ++countTaskId;
        this.name = name;
        this.description = description;
        this.startTime = setStartTime(startTime);
        this.duration = duration;
        this.status = TaskStatuses.NEW; // все новые задачи создаются по умолчанию со статусом NEW
    }

    // Конструктор для новых задач со счетчиком для id.
    public Task(String name, String description) {
        id = ++countTaskId;
        this.name = name;
        this.description = description;
        this.status = TaskStatuses.NEW; // все новые задачи создаются по умолчанию со статусом NEW
    }

    // Конструктор для обновления задач (с 8-го спринта и для создания из файла) без счетчика и с обновлением статуса.
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public static void setCountTaskId(int countTaskId) {
        Task.countTaskId = countTaskId;
    }

    protected LocalDateTime setStartTime(String startTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startTimeLDT = LocalDateTime.parse(startTimeStr, formatter);
        if (startTimeLDT.isBefore(LocalDateTime.now())) {
            throw new ManagerSaveException("Время начала выполнения задачи не должно быть раньше текущего времени");
        }
        return startTimeLDT;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,",
                getId(),
                getType(),
                getName(),
                getStatus(),
                getDescription(),
                getStartTime().toString(),
                getEndTime().toString());
    }
}
