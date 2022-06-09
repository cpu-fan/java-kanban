public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected String status;
    protected static int count;

    // Конструктор для новых задач со счетчиком для id.
    public Task(String name, String description) {
        count++;
        id = count;
        this.name = name;
        this.description = description;
        this.status = "NEW"; // все новые задачи создаются по умолчанию со статусом NEW
    }

    // Конструктор для обновления задач без счетчика и с обновлением статуса.
    public Task(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // Конструктор для обновления эпика без счетчика и статуса, т.к. последний рассчитывается менеджером.
    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
