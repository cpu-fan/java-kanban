package tasktracker.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tasktracker.historymanager.HistoryManager;
import tasktracker.historymanager.InMemoryHistoryManager;
import tasktracker.http.adapters.LocalDateTimeAdapter;
import tasktracker.taskmanager.FileBackedTaskManager;
import tasktracker.taskmanager.HTTPTaskManager;
import tasktracker.taskmanager.TaskManager;

import java.io.File;
import java.time.LocalDateTime;

public final class Managers {

    private static final File FILE_PATH = new File("src/main/resources/fileForSave.csv");
    private static final String URL = "http://localhost:8078";

    private Managers() {}

    public static TaskManager getDefaultFile() {
        return new FileBackedTaskManager(FILE_PATH);
    }

    public static TaskManager getDefault() {
        return new HTTPTaskManager(URL);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static File getFilePath() {
        return FILE_PATH;
    }

    public static String getUrl() {
        return URL;
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
