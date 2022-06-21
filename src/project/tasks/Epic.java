package project.tasks;

import java.util.HashMap;

import static project.tasks.TaskStatuses.*;

public class Epic extends Task {
    private HashMap<Integer, Subtask> epicSubtasks;

    // Конструктор для создания эпика.
    public Epic(String name, String description) {
        super(name, description);
        epicSubtasks = new HashMap<>();
        calculateEpicStatus();
    }

    // Конструктор для обновления эпика.
    public Epic(int id, String name, String description) {
        this(name, description);
        this.id = id;
    }

    public void calculateEpicStatus() {
        // Считаем количество подзадач со статусом NEW и DONE.
        int countNew = 0;
        int countDone = 0;
        if (epicSubtasks != null) {
            for (Subtask subtask : epicSubtasks.values()) {
                switch (subtask.status) {
                    case NEW:
                        countNew++;
                        break;
                    case DONE:
                        countDone++;
                        break;
                }
            }

            // В зависимости от количества задач с определенным статусом устанавливаем статус для эпика.
            if (epicSubtasks.isEmpty() || epicSubtasks.size() == countNew) {
                this.status = NEW;
            } else if (epicSubtasks.size() == countDone) {
                this.status = DONE;
            } else {
                this.status = IN_PROGRESS;
            }
        }
    }

    public void addSubtask(Subtask subtask) {
        epicSubtasks.put(subtask.id, subtask);
        calculateEpicStatus();
    }

    public void deleteSubtask(int subtaskId) {
        epicSubtasks.remove(subtaskId);
        calculateEpicStatus();
    }

    public void clearSubtask() {
        epicSubtasks.clear();
        calculateEpicStatus();
    }

    public HashMap<Integer, Subtask> getEpicSubtasks() {
        return epicSubtasks;
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
