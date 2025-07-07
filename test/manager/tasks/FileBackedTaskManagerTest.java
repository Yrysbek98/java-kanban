package manager.tasks;

import com.yandex.hw.manager.tasks.FileBackedTaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import org.junit.jupiter.api.Test;

import java.io.*;
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

        List<String> lines = Files.readAllLines(testFile.toPath());
        assertEquals(5, lines.size(), "Неверное количество строк в файле");
        assertTrue(lines.get(1).startsWith("1,TASK"), "Неверный формат задачи");
        assertTrue(lines.get(2).startsWith("2,TASK"), "Неверный формат эпика");
        assertTrue(lines.get(3).contains("3,TASK"), "Неверный формат подзадачи");
    }


}
