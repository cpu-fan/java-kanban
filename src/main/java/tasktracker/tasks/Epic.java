package tasktracker.tasks;

import java.time.LocalDateTime;
import java.util.HashMap;

import static tasktracker.tasks.TaskStatuses.*;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> epicSubtasks;
    private LocalDateTime endTime;

    // Конструктор для создания эпика.
    public Epic(String name, String description) {
        super(name, description);
        epicSubtasks = new HashMap<>();
        calculateEpicStatus();
        calculateEpicDuration();
    }

    // Конструктор для обновления эпика.
    public Epic(int id, String name, String description) {
        this(name, description);
        this.id = id;
    }

    // Конструктор для создания эпика из строки.
    public Epic(int id, String name, String description, TaskStatuses status) {
        this(id, name, description);
        this.status = status;
    }

    // Конструктор для создания эпика из строки с временем.
    public Epic(int id, String name, String description, TaskStatuses status, String startTime, int duration) {
        this(id, name, description, status);
        this.startTime = setStartTime(startTime);
        this.duration = duration;
    }

    public void addSubtask(Subtask subtask) {
        epicSubtasks.put(subtask.id, subtask);
        calculateEpicStatus();
        calculateEpicDuration();
    }

    public void deleteSubtask(int subtaskId) {
        epicSubtasks.remove(subtaskId);
        calculateEpicStatus();
        calculateEpicDuration();
    }

    public void clearSubtask() {
        epicSubtasks.clear();
        calculateEpicStatus();
        calculateEpicDuration();
    }

    public HashMap<Integer, Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }

    private void calculateEpicStatus() {
        // Считаем количество подзадач со статусом NEW и DONE.
        int countNew = 0;
        int countDone = 0;
        if (!epicSubtasks.isEmpty()) {
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

    private void calculateEpicDuration() {
        if (!epicSubtasks.isEmpty()) {
            for (Subtask subtask : epicSubtasks.values()) {
                if (subtask.getStartTime() != null) {
                    LocalDateTime epicStartTime = LocalDateTime.MAX;
                    LocalDateTime epicEndTime = LocalDateTime.MIN;
                    long epicDuration = 0;

                    if (subtask.getStartTime().isBefore(epicStartTime)) {
                        epicStartTime = subtask.getStartTime();
                    }
                    if (subtask.getEndTime().isBefore(epicEndTime)) {
                        epicEndTime = subtask.getEndTime();
                    }
                    epicDuration += subtask.duration;

                    this.startTime = epicStartTime;
                    this.endTime = epicEndTime;
                    this.duration = epicDuration;
                } else {
                    this.startTime = null;
                    this.endTime = null;
                    this.duration = 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
