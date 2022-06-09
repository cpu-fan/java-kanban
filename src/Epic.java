import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, Subtask> epicSubtasks;

    // Конструктор для создания эпика.
    public Epic(String name, String description) {
        super(name, description);
        epicSubtasks = new HashMap<>();
        calculateEpicStatus();
    }

    // Конструктор для обновления эпика.
    public Epic(int id, String name, String description, HashMap<Integer, Subtask> epicSubtasks) {
        super(id, name, description);
        this.epicSubtasks = epicSubtasks;
        calculateEpicStatus();
    }

    public void calculateEpicStatus() {
        // Считаем количество подзадач со статусом NEW и DONE.
        int countNew = 0;
        int countDone = 0;
        for (Subtask subtask : epicSubtasks.values()) {
            switch (subtask.status) {
                case "NEW":
                    countNew++;
                    break;
                case "DONE":
                    countDone++;
            }
        }

        // В зависимости от количества задач с определенным статусом устанавливаем статус для эпика.
        if (epicSubtasks.isEmpty() || epicSubtasks.size() == countNew) {
            this.status = "NEW";
        } else if (epicSubtasks.size() == countDone) {
            this.status = "DONE";
        } else {
            this.status = "IN_PROGRESS";
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", epicSubtasks=" + epicSubtasks +
                '}';
    }
}
