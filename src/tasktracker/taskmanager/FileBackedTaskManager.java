package tasktracker.taskmanager;

import tasktracker.exceptions.ManagerSaveException;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBackedTaskManager extends InMemoryTaskManager {
    String fileName;
    String taskToString;

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    public void save() throws ManagerSaveException {
        try (Writer fw = new FileWriter(fileName, true)) {
            fw.write("\n" + taskToString);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        taskToString = Task.toString(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }
}
