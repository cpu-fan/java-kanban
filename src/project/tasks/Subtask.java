package project.tasks;

public class Subtask extends Task {
    private String epicName;
    private int epicId;

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

    public String getEpicName() {
        return epicName;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                ", epicName='" + epicName + '\'' +
                '}';
    }
}
