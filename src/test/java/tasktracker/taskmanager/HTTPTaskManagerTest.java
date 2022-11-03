package tasktracker.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasktracker.http.KVServer;
import tasktracker.managers.Managers;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {

    KVServer kvServer;
    TaskManager manager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();

        task = new Task("task1", "desc", "12.12.2323 12:00", 60);
        epic = new Epic("epic1", "desc");
        subtask = new Subtask("subtask1", "desc", epic, "12.12.2323 14:00", 60);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        // заполняем историю
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());
    }

    @AfterEach
    void tearDown() {
        kvServer.stop(0);
    }

    @Test
    void save() {
        HTTPTaskManager managerForLoad = new HTTPTaskManager(Managers.getUrl());
        managerForLoad.load();

        assertEquals(manager.toString(), managerForLoad.toString());
    }

    @Test
    void load() {
        HTTPTaskManager managerForLoad = new HTTPTaskManager(Managers.getUrl());
        managerForLoad.load();

        assertEquals(manager.getListAllTasks().toString(), managerForLoad.getListAllTasks().toString());
        assertEquals(manager.getListAllEpics().toString(), managerForLoad.getListAllEpics().toString());
        assertEquals(manager.getListAllSubtasks().toString(), managerForLoad.getListAllSubtasks().toString());
        assertEquals(manager.getHistory().toString(), managerForLoad.getHistory().toString());
    }
}