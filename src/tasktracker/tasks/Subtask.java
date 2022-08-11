package tasktracker.tasks;

public class Subtask extends Task {
    private String epicName;
    private final int epicId;

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
    public Subtask(int subtaskId, String name, String description, String status, int epicId) {
        super(subtaskId, name, description, status);
        this.epicId = epicId;
    }

    public String getEpicName() {
        return epicName;
    }

    public int getEpicId() {
        return epicId;
    }

    // Унаследованный toString от класса Task с добавлением в конец id эпика, в котором содержится эта сабтаска
    @Override
    public String toString() {
        return super.toString() + getEpicId();
    }
}
