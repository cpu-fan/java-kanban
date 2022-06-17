package project.main;

import project.manager.Manager;
import project.tasks.Epic;
import project.tasks.Subtask;
import project.tasks.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        // Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = new Task("task1", "desc for task1");
        Task task2 = new Task("task2", "desc for task2");
        Epic epic1 = new Epic("epic1", "desc for epic1");
        Epic epic2 = new Epic("epic2", "desc for epic2");
        Subtask subtask1 = new Subtask("subtask1", "desc for subtask1", epic1);
        Subtask subtask2 = new Subtask("subtask2", "desc for subtask2", epic1);
        Subtask subtask3 = new Subtask("subtask3", "desc for subtask3", epic2);

        // помещение задач в свои коллекции для хранения
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        // Распечатайте списки эпиков, задач и подзадач, через System.out.println().
        System.out.println(manager.getListAllTasks());
        System.out.println(manager.getListAllSubtasks());
        System.out.println(manager.getListAllEpics());
        System.out.println();

        // Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач.
        task1 = new Task(task1.getId(), "Обновление задачи", "Описываю новое обновление и меняю статус",
                "IN_PROGRESS");
        manager.updateTask(task1);

        subtask1 = new Subtask(subtask1.getId(), "Обновление подзадачи из эпика 1",
                "Подзадача 1, ее обновление и замена статуса", "IN_PROGRESS", epic2);
        manager.updateSubtask(subtask1);

        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getSubtaskById(subtask1.getId()));
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println(manager.getEpicById(epic2.getId()));
        System.out.println();

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.removeSubtaskById(task1.getId());
        manager.removeEpicById(epic2.getId());
    }
}
