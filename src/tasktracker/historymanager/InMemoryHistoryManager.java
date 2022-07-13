package tasktracker.historymanager;

import tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотра задач
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyList=" + historyList +
                '}';
    }
}
