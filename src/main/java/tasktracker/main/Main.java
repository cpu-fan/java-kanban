package tasktracker.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tasktracker.api.HttpTaskServer;
import tasktracker.api.KVServer;
import tasktracker.api.KVTaskClient;
import tasktracker.api.LocalDateTimeAdapter;
import tasktracker.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();

        Task task1 = new Task("task1", "desc", "12.12.2323 12:00", 60);
        String value = gson.toJson(task1);
        kvTaskClient.put("task1", value);
        System.out.println(kvTaskClient.load("task1"));
    }
}
