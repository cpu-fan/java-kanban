package tasktracker.taskmanager;

import tasktracker.exceptions.ManagerSaveException;
import tasktracker.historymanager.InMemoryHistoryManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskTypes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    String fileName;

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    public void save() throws ManagerSaveException {
        try (Writer fw = new FileWriter(fileName)) {
            fw.write("id,type,name,status,description,epic\n");
            fw.write(String.format("%s", super.toString()));
            fw.write("\n");
            fw.write(String.format("%s", super.toStringHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
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

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public ArrayList<Task> getListAllTasks() {
        return super.getListAllTasks();
    }

    @Override
    public ArrayList<Epic> getListAllEpics() {
        return super.getListAllEpics();
    }

    @Override
    public ArrayList<Subtask> getListAllSubtasks() {
        return super.getListAllSubtasks();
    }
}
