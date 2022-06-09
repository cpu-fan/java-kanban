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
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println();

        // Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач.
        task1 = new Task(task1.id, "Обновление задачи", "Описываю новое обновление и меняю статус",
                "IN_PROGRESS");
        subtask1 = new Subtask(subtask1.id, "Обновление подзадачи из эпика 1",
                "Подзадача 1, ее обновление и замена статуса", "IN_PROGRESS", epic1);

        System.out.println(task1);
        System.out.println(subtask1);
        System.out.println(epic1);
        System.out.println();

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.removeSubtaskById(subtask1.id);
        manager.removeEpicById(epic2.id);
        System.out.println(epic1);
    }
}
