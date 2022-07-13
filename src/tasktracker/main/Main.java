package tasktracker.main;

import tasktracker.customclasses.CustomLinkedList;
import tasktracker.managers.Managers;
import tasktracker.taskmanager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        CustomLinkedList<Task> taskCustomLinkedList = new CustomLinkedList<>();

        taskCustomLinkedList.linkLast(new Task("name1", "desc1"));
        taskCustomLinkedList.linkLast(new Task("name2", "desc2"));
        taskCustomLinkedList.linkLast(new Task("name3", "desc3"));
        taskCustomLinkedList.linkLast(new Task("name4", "desc4"));
        taskCustomLinkedList.linkLast(new Task("name5", "desc5"));

        System.out.println(taskCustomLinkedList.size());
    }
}
