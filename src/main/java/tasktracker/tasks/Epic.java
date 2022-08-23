package tasktracker.tasks;

import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
            int minSubtaskId = Collections.min(epicSubtasks.keySet());
            int maxSubtaskId = Collections.max(epicSubtasks.keySet());
            this.startTime = epicSubtasks.get(minSubtaskId).startTime;
            this.endTime = epicSubtasks.get(maxSubtaskId).getEndTime();
            this.duration = ChronoUnit.MINUTES.between(startTime, endTime);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
