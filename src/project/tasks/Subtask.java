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
    /* Для решения проблемы удаления подзадачи из старого эпика, ничего лучше пока не придумал, кроме как передавать в
    * конструктор два эпика: старый, откуда перемещаю, и новый, куда перемещаю. Если в смене эпика нет необходимости,
    * то указать необходимо один и тот же эпик в качестве аргументов. В таком случае удаление происходить не будет. */
    public Subtask(Subtask subtask, String name, String description, String status, Epic oldEpic, Epic newEpic) {
        super(subtask.id, name, description, status);
        this.epicName = newEpic.name;
        if (newEpic.id != oldEpic.id) {
            oldEpic.deleteSubtask(this.id);
        }
        this.epicId = newEpic.id;
        newEpic.addSubtask(this); // обновляем подзадачу в ее эпике и пересчитываем статус
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
