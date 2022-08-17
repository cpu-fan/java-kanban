package tasktracker.managers;

import tasktracker.historymanager.HistoryManager;
import tasktracker.historymanager.InMemoryHistoryManager;
import tasktracker.taskmanager.InMemoryTaskManager;
import tasktracker.taskmanager.TaskManager;

public final class Managers {

    private Managers() {}

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
