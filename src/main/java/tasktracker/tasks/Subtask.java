package tasktracker.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private String epicName;
    private final int epicId;

    // Конструктор для обновления подзадачи и помещения в эпик со временем.
    public Subtask(int subtaskId, String name, String description, TaskStatuses status, Epic epic,
                   String startTime, int duration) {
        super(subtaskId, name, description, status);
        this.epicName = epic.name;
        this.epicId = epic.id;
        this.startTime = setStartTime(startTime);
        this.duration = duration;
        epic.addSubtask(this);
    }

    // Конструктор для создания новой подзадачи и помещения в эпик со временем.
    public Subtask(String name, String description, Epic epic, String startTime, int duration) {
        super(name, description);
        this.epicName = epic.name;
        this.epicId = epic.id;
        this.startTime = setStartTime(startTime);
        this.duration = duration;
        epic.addSubtask(this);
    }

    // Конструктор для создания новой подзадачи и помещения в эпик.
    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epicName = epic.name;
        this.epicId = epic.id;
        epic.addSubtask(this);
    }

    // Конструктор для обновления подзадачи.
    public Subtask(int subtaskId, String name, String description, TaskStatuses status, Epic epic) {
        super(subtaskId, name, description, status);
        this.epicName = epic.name;
        this.epicId = epic.id;
    }

    // Конструктор для сохранения сабтаски из строки.
    public Subtask(int subtaskId, String name, String description, TaskStatuses status, int epicId) {
        super(subtaskId, name, description, status);
        this.epicId = epicId;
    }

    // Конструктор для сохранения сабтаски из строки.
    public Subtask(int subtaskId, String name, String description, TaskStatuses status, int epicId,
                   String startTime, int duration) {
        this(subtaskId, name, description, status, epicId);
        this.startTime = setStartTime(startTime);
        this.duration = duration;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.SUBTASK;
    }

    // Унаследованный toString от класса Task с добавлением в конец id эпика, в котором содержится эта сабтаска
    @Override
    public String toString() {
        return super.toString() + getEpicId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId && Objects.equals(epicName, subtask.epicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicName, epicId) * 31;
    }
}
