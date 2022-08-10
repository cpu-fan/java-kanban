package tasktracker.taskmanager;

import tasktracker.exceptions.ManagerSaveException;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {
    File fileName;

    public FileBackedTaskManager(File fileName) {
        this.fileName = fileName;
    }

    public void save() {
        try (Writer fw = new FileWriter(fileName)) {
            fw.write("id,type,name,status,description,epic\n");
            fw.write(String.format("%s", super.toString()));
            fw.write("\n");
            fw.write(String.format("%s", super.historyToString()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public Task fromString(String line) {
        Task task = new Task(0, "", "", "");

        // Сами линии (таски) разбиваем на элементы - id, type, name и т.д. и возвращаем определенную таску
        String[] elem = line.split(",");
        switch (elem[1]) {
            case "TASK":
                return new Task(Integer.parseInt(elem[0]), elem[2], elem[4], elem[3]);
            case "EPIC":
                return new Epic(Integer.parseInt(elem[0]), elem[2], elem[4]);
            case "SUBTASK":
                return new Subtask(Integer.parseInt(elem[0]), elem[2], elem[4], elem[3], Integer.parseInt(elem[5]));
        }
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            // Сохраняем текст из файла в переменную text
            String text = Files.readString(file.toPath());
            // Разбиваем текст на линии, которые представляют собой информацию о таске
            String[] lines = text.split(System.lineSeparator());
            for (int i = 1; i < lines.length; i++) {
                switch (lines[i].split(",")[1]) {
                    case "TASK":
                        Task task = taskManager.fromString(lines[i]);
                        taskManager.createTask(task);
                        break;
                    case "EPIC":
                        Task epic = taskManager.fromString(lines[i]);
                        taskManager.createEpic((Epic) epic);
                        break;
                    case "SUBTASK":
                        Task subtask = taskManager.fromString(lines[i]);
                        taskManager.createSubtask((Subtask) subtask);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // здесь свое или стандартное?
        }
        return taskManager;
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
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        super.removeSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}
