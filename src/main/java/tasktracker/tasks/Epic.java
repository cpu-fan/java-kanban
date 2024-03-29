package tasktracker.tasks;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

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
        if (subtask.getEpicName() == null) { // новая проверка в 8 ТЗ при обновлении сабтаски по http
            subtask.setEpicName(this.name);
        }
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

    @Override
    public TaskTypes getType() {
        return TaskTypes.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String getEndTimeInFormat() {
        return endTime == null ? null : endTime.format(Task.FORMATTER);
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
            if (epicSubtasks.size() == countNew) {
                this.status = NEW;
            } else if (epicSubtasks.size() == countDone) {
                this.status = DONE;
            } else {
                this.status = IN_PROGRESS;
            }
        }
    }

    private void calculateEpicDuration() {
        if (!epicSubtasks.isEmpty() && epicSubtasks.size() > 1) {
            LocalDateTime epicStartTime = LocalDateTime.MAX;
            LocalDateTime epicEndTime = LocalDateTime.MIN;
            long epicDuration = 0;

            for (Subtask subtask : epicSubtasks.values()) {
                if (subtask.getStartTime() != null && subtask.getStartTime().isBefore(epicStartTime)) {
                    epicStartTime = subtask.getStartTime();
                }
                if (subtask.getEndTime() != null && subtask.getEndTime().isAfter(epicEndTime)) {
                    epicEndTime = subtask.getEndTime();
                }
                epicDuration += subtask.duration;
            }

            this.startTime = epicStartTime;
            this.endTime = epicEndTime;
            this.duration = epicDuration;

        } else if (epicSubtasks.size() == 1) {
            int subtaskId = 0;
            for (Integer id : epicSubtasks.keySet()) {
                subtaskId = id;
            }
            this.startTime = epicSubtasks.get(subtaskId).getStartTime();
            this.endTime = epicSubtasks.get(subtaskId).getEndTime();
            this.duration = epicSubtasks.get(subtaskId).getDuration();

        } else {
            this.startTime = null;
            this.endTime = null;
            this.duration = 0;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(epicSubtasks, epic.epicSubtasks) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicSubtasks, endTime) * 31;
    }
}
