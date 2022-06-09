public class Subtask extends Task {
    String epicName;
    int epicId;

    // Конструктор для создания новой подзадачи и помещения в эпик.
    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epicName = epic.name;
        this.epicId = epic.id;
        epic.epicSubtasks.put(this.id, this);
    }

    // Конструктор для обновления подзадачи.
    public Subtask(int id, String name, String description, String status, Epic epic) {
        super(id, name, description, status);
        this.epicName = epic.name;
        this.epicId = epic.id;
        epic.epicSubtasks.put(this.id, this); // обновляем подзадачу в ее эпике
        epic.calculateEpicStatus(); // пересчитываем статус
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
