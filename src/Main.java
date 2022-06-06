public class Main {

    public static void main(String[] args) {
        System.out.println("\nПоехали!\n");

        Manager manager = new Manager();

        manager.createTask(new Task("my first task", "created task to check the work class"));
        manager.createTask(new Task("my second task", "re-created task"));
        manager.createTask(new Epic("my first epic", "проверим увеличивается ли id"));

        System.out.println(manager.mapOfTasksAllTypes.size());
        System.out.println(manager);
        System.out.println(manager.getListAllTasks());
        System.out.println(manager.getListAllTasks().size());

        System.out.println(manager.getTaskById(2));

        manager.updateTask(2, "русификация задачи", "моя вторая задача после смены языка");
        System.out.println(manager.getTaskById(2));

    }
}
