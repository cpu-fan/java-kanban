package tasktracker.taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasktracker.exceptions.NonExistentTaskException;
import tasktracker.managers.Managers;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.tasks.TaskStatuses;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest<T extends TaskManager> {

    TaskManager manager = Managers.getDefault();
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void setUp() {
        task = new Task("name", "desc");
        epic = new Epic("name", "desc");
        subtask = new Subtask("name", "desc", epic);
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
    }

    @AfterEach
    void tearDown() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
    }

    @Test
    void createTaskShouldAddTaskInManagerMapOfTasks() {
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void createEpicShouldAddEpicInManagerMapOfEpics() {
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void createSubtaskShouldAddSubtaskInManagerMapOfSubtasks() {
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

    // Для подзадач нужно дополнительно проверить наличие эпика
    @Test
    void shouldBeEpicInSubtask() {
        assertEquals(manager.getEpicById(epic.getId()).getId(), manager.getSubtaskById(subtask.getId()).getEpicId());
    }

    // а для эпика — расчёт статуса
    @Test
    void epicStatusShouldBeNew() {
        assertEquals(TaskStatuses.NEW, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void epicStatusShouldBeInProgress() {
        subtask = new Subtask(subtask.getId(), "update", "update desc", TaskStatuses.IN_PROGRESS, epic);
        manager.updateSubtask(subtask);
        assertEquals(TaskStatuses.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void epicStatusShouldBeDone() {
        subtask = new Subtask(subtask.getId(), "update", "update desc", TaskStatuses.DONE, epic);
        manager.updateSubtask(subtask);
        assertEquals(TaskStatuses.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    // Для каждого метода нужно проверить его работу:
    // * со стандартным поведением;
    // * c пустым списком задач;
    // * с неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
    @Test
    void getTaskById() {
        assertEquals(task.getId(), manager.getTaskById(task.getId()).getId());
    }

    @Test
    void shouldReturnNullOnEmptyMapOfTasks() {
        manager.deleteAllTasks();
        assertNull(manager.getTaskById(1));
        /* если я правильно понял, то данный тест (и последующие аналогичные) подходит для двух кейсов из требований:
        с пустым списком задач и неверным идентификатором задачи (несуществующий) */
    }

    @Test
    void getEpicById() {
        assertEquals(epic.getId(), manager.getEpicById(epic.getId()).getId());
    }

    @Test
    void shouldReturnNullOnEmptyMapOfEpics() {
        manager.deleteAllEpics();
        assertNull(manager.getEpicById(1));
    }

    @Test
    void getSubtaskById() {
        assertEquals(subtask.getId(), manager.getSubtaskById(subtask.getId()).getId());
    }

    @Test
    void shouldReturnNullOnEmptyMapOfSubtasks() {
        manager.deleteAllSubtasks();
        assertNull(manager.getSubtaskById(1));
    }

    @Test
    void updateTask() {
        task = new Task("update subtask", "desc", "25.08.2022 23:59", 60);
        manager.updateTask(task);
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void updateTaskWhenMapOfTasksIsEmpty() {
        manager.deleteAllTasks();
        manager.updateTask(task);
        assertEquals(task, manager.getTaskById(task.getId()));
        // Апдейт на пустую мапу задач работает как обычное добавление (аналогичное действие у эпика и подзадачи)
    }

    @Test
    void updateTaskInvalidIdMapOfTasks() {
        manager.deleteAllTasks();
        assertThrows(NonExistentTaskException.class, () -> manager.updateTask(manager.getTaskById(task.getId())));
    }

    @Test
    void updateEpic() {
        epic = new Epic(epic.getId(), "epic updated", "desc");
        manager.updateEpic(epic);
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void updateEpicWhenMapOfEpicsIsEmpty() {
        manager.deleteAllEpics();
        manager.updateEpic(epic);
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void updateEpicInvalidIdMapOfEpics() {
        manager.deleteAllEpics();
        assertThrows(NonExistentTaskException.class, () -> manager.updateEpic(manager.getEpicById(epic.getId())));
    }

    @Test
    void updateSubtask() {
        subtask = new Subtask(subtask.getId(), "updated subtask", "desc",
                TaskStatuses.IN_PROGRESS, epic);
        manager.updateSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

    @Test
    void updateSubtaskWhenMapOfSubtasksIsEmpty() {
        manager.deleteAllSubtasks();
        assertThrows(NonExistentTaskException.class, () -> manager.updateSubtask(manager.getSubtaskById(subtask.getId())));
    }

    @Test
    void updateSubtaskWhenInvalidIdMapOfSubtasks() {
        manager.deleteAllSubtasks();
        assertThrows(NonExistentTaskException.class, () -> manager.updateSubtask(manager.getSubtaskById(-1)));
    }

    @Test
    void removeTaskById() {
        manager.removeTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()));
    }

    @Test
    void removeTaskByIdWhenMapOfTasksIsEmpty() throws IOException {
        manager.deleteAllTasks();
        assertThrows(NonExistentTaskException.class, () -> manager.removeTaskById(task.getId()));
    }

    @Test
    void removeTaskByIdWhenInvalidIdMapOfTasks() throws IOException {
        manager.deleteAllTasks();
        assertThrows(NonExistentTaskException.class, () -> manager.removeTaskById(-1));
    }

    @Test
    void removeEpicById() {
        manager.removeEpicById(epic.getId());
        assertNull(manager.getEpicById(epic.getId()));
    }
    @Test
    void removeEpicByIdWhenMapOfEpicsIsEmpty() throws IOException {
        manager.deleteAllEpics();
        assertThrows(NonExistentTaskException.class, () -> manager.removeEpicById(epic.getId()));
    }

    @Test
    void removeEpicByIdWhenInvalidIdMapOfEpics() throws IOException {
        manager.deleteAllEpics();
        assertThrows(NonExistentTaskException.class, () -> manager.removeEpicById(-1));
    }
    @Test
    void removeSubtaskById() {
        manager.removeSubtaskById(subtask.getId());
        assertNull(manager.getSubtaskById(subtask.getId()));
    }
    @Test
    void removeSubtaskByIdWhenMapOfSubtasksIsEmpty() throws IOException {
        manager.deleteAllSubtasks();
        assertThrows(NonExistentTaskException.class, () -> manager.removeSubtaskById(subtask.getId()));
    }

    @Test
    void removeSubtaskByIdWhenInvalidIdMapOfSubtasks() throws IOException {
        manager.deleteAllSubtasks();
        assertThrows(NonExistentTaskException.class, () -> manager.removeSubtaskById(-1));
    }

    @Test
    void deleteAllTasks() {
        assertEquals(1, manager.getListAllTasks().size());
        manager.deleteAllTasks();
        assertEquals(0, manager.getListAllTasks().size());
    }

    @Test
    void deleteAllEpics() {
        assertEquals(1, manager.getListAllEpics().size());
        manager.deleteAllEpics();
        assertEquals(0, manager.getListAllEpics().size());
    }

    @Test
    void deleteAllSubtasks() {
        assertEquals(1, manager.getListAllSubtasks().size());
        manager.deleteAllSubtasks();
        assertEquals(0, manager.getListAllSubtasks().size());
    }
}