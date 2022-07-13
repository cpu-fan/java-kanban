package tasktracker.customclasses;

public class CustomLinkedList<Task> {
    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    public int size() {
        return this.size;
    }
}
