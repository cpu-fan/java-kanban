package tasktracker.historymanager;

import tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотра задач
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (historyList.size() >= 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyList=" + historyList +
                '}';
    }
}
