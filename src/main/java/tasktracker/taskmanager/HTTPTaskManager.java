package tasktracker.taskmanager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import tasktracker.http.KVTaskClient;
import tasktracker.managers.Managers;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTaskManager {

    private static KVTaskClient kvTaskClient;
    private static Gson gson;

    public HTTPTaskManager(String url) {
        super(null);
        kvTaskClient = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(super.getListAllTasks()));
        kvTaskClient.put("epics", gson.toJson(super.getListAllEpics()));
        kvTaskClient.put("subtasks", gson.toJson(super.getListAllSubtasks()));

        List<Integer> history = super.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        kvTaskClient.put("history", gson.toJson(history));
    }

    public void load() {
        JsonArray tasksArray = JsonParser.parseString(kvTaskClient.load("tasks")).getAsJsonArray();
        JsonArray epicsArray = JsonParser.parseString(kvTaskClient.load("epics")).getAsJsonArray();
        JsonArray subtasksArray = JsonParser.parseString(kvTaskClient.load("subtasks")).getAsJsonArray();
        JsonArray historyArray = JsonParser.parseString(kvTaskClient.load("history")).getAsJsonArray();

        for (JsonElement taskElement : tasksArray) {
            Task task = gson.fromJson(taskElement.toString(), Task.class);
            this.createTask(task);
        }

        for (JsonElement epicElement : epicsArray) {
            Epic epic = gson.fromJson(epicElement.toString(), Epic.class);
            this.createEpic(epic);
        }

        for (JsonElement subtaskElement : subtasksArray) {
            Subtask subtask = gson.fromJson(subtaskElement.toString(), Subtask.class);
            this.createSubtask(subtask);
        }

        for (JsonElement historyElement : historyArray) {
            int id = historyElement.getAsInt();
            if (this.getMapOfTasks().containsKey(id)) {
                this.getTaskById(id);
            } else if (this.getMapOfEpics().containsKey(id)) {
                this.getEpicById(id);
            } else {
                this.getSubtaskById(id);
            }
        }
    }
}
