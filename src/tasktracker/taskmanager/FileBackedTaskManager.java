package tasktracker.taskmanager;

import tasktracker.exceptions.ManagerSaveException;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskTypes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBackedTaskManager extends InMemoryTaskManager {
    String fileName;
//    String toString; // 1

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }
    // 2
    // Напишите метод сохранения задачи в строку String toString(Task task) или переопределите базовый.
//    public String toStringForFile(Task task) {
//        String taskType; // для изменения из enum в String
//        String epicId; // для изменения из int в String (для Task и Epic присваивается пустое значение - "")
//
//        if (task.getClass().equals(Task.class)) {
//            taskType = String.valueOf(TaskTypes.TASK);
//            epicId = "";
//        } else if (task.getClass().equals(Epic.class)) {
//            taskType = String.valueOf(TaskTypes.EPIC);
//            epicId = "";
//        } else {
//            taskType = String.valueOf(TaskTypes.SUBTASK);
//            epicId = String.valueOf(task.getEpicId());
//        }
//
//        return String.format("%s, %s, %s, %s, %s, %s\n",
//                task.getId(),
//                taskType,
//                task.getName(),
//                task.getStatus(),
//                task.getDescription(),
//                epicId);
//    }

//    public String toStringForFile(InMemoryTaskManager manager) {
//        return manager.toString();
//    }

    public void save() throws ManagerSaveException {
        try (Writer fw = new FileWriter(fileName)) {
            fw.write("id,type,name,status,description,epic\n");
            // 2
//            for (Task task : super.getListAllTasks()) {
//                fw.write(toStringForFile(task));
//            }

            fw.write(String.format("%s", getInMemoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
//        toString = Task.toString(task); // 1
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
//        toString = Epic.toString(epic); // 1
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
//        toString = Subtask.toString(subtask); // 1
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
//        toString = Task.toString(task); // 1
        save();
    }
}
