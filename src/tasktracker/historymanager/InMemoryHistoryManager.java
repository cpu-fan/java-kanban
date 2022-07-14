package tasktracker.historymanager;

import tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотра задач
    private final List<Task> historyList = new ArrayList<>(); // old version, потом удалить

    // (новая реализация) Хэш-таблица для хранения связки id и узлов
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node head;
    private Node tail;

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    @Override
    public void addToHistory(Task task) {
        linkLast(task);
        nodeMap.put(task.getId(), tail);
//        historyList.add(task); // old version
    }

    @Override
    public List<Task> getHistory() {
//        return historyList; // old version
        List<Task> historyList = new ArrayList<>();
        for (Node value : nodeMap.values()) {
            historyList.add(value.task);
        }
//        List<Task> historyList = new ArrayList<>();
//        Node node = head;
//        while (head != null) {
//            historyList.add(node.task);
//            if (node.next == null) break;
//            node = node.next;
//        }
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

    public static class Node {
        private Node prev;
        private Task task;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
