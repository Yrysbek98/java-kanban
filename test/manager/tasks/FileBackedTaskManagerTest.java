package manager.tasks;

import com.yandex.hw.manager.tasks.FileBackedTaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    void shouldHandleEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFile(emptyFile);
        assertTrue(loadedManager.getAllTask(Task.class).isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.getAllTask(Epic.class).isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.getAllTask(Subtask.class).isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void shouldSaveMultipleTasks() throws IOException {
        File testFile = File.createTempFile("multiSave", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(testFile);
        manager.addTask(new Task("Task 1", "Description 1", TaskStatus.NEW));
        manager.addTask(new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS));
        manager.addTask(new Task("Task 3", "Description 3", TaskStatus.DONE));

        List<String> lines = Files.readAllLines(testFile.toPath(), StandardCharsets.UTF_8);

        assertEquals(5, lines.size(), "Неверное количество строк");
        assertTrue(lines.get(4).isEmpty(), "Последняя строка должна быть пустой");

        String[] task1Parts = lines.get(1).split(",");
        assertEquals("1", task1Parts[0]);
        assertEquals("TASK", task1Parts[1]);
        assertEquals("Task 1", task1Parts[2]);
        assertEquals("NEW", task1Parts[4]);

        String[] task2Parts = lines.get(2).split(",");
        assertEquals("2", task2Parts[0]);
        assertEquals("TASK", task2Parts[1]);
        assertEquals("Task 2", task2Parts[2]);
        assertEquals("IN_PROGRESS", task2Parts[4]);

        String[] task3Parts = lines.get(3).split(",");
        assertEquals("3", task3Parts[0]);
        assertEquals("TASK", task3Parts[1]);
        assertEquals("Task 3", task3Parts[2]);
        assertEquals("DONE", task3Parts[4]);


    }


}
