public class Main {

    public static void main(String[] args) {
        System.out.println("\nПоехали!\n");

        Task task = new Task("my first task", "created task to check the work class");
        Task task2 = new Task("my second task", "re-created task");
        Epic epic = new Epic("my first epic", "проверим увеличивается ли id");

        Manager manager = new Manager(task);
    }
}
