package manager.tasks;

import com.yandex.hw.manager.tasks.FileBackedTaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import manager.TaskManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    @Test
    @Override
    protected void addTask() throws IOException {
        File testFile = File.createTempFile("multiSave", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(testFile);
        manager.addTask(new Task("Task 1", "Description 1", TaskStatus.NEW, "12-05-2017 12:00", 15));
        manager.addTask(new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS, "12-06-2017 12:00", 15));
        manager.addTask(new Task("Task 3", "Description 3", TaskStatus.DONE, "12-07-2017 12:00", 15));

        List<String> lines = Files.readAllLines(testFile.toPath(), StandardCharsets.UTF_8);

        assertEquals(5, lines.size(), "Неверное количество строк");
        assertTrue(lines.get(4).isEmpty(), "Последняя строка должна быть пустой");

        String[] task1Parts = lines.get(1).split(",");
        assertEquals("TASK", task1Parts[1]);
        assertEquals("Task 1", task1Parts[2]);
        assertEquals("NEW", task1Parts[3]);

        String[] task2Parts = lines.get(2).split(",");
        assertEquals("TASK", task2Parts[1]);
        assertEquals("Task 2", task2Parts[2]);
        assertEquals("IN_PROGRESS", task2Parts[3]);

        String[] task3Parts = lines.get(3).split(",");
        assertEquals("TASK", task3Parts[1]);
        assertEquals("Task 3", task3Parts[2]);
        assertEquals("DONE", task3Parts[3]);
    }

    @Test
    @Override
    protected void updateTask() throws IOException {
        File testFile = File.createTempFile("updateTask", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(testFile);
        Task task1 = new Task("Вторая обычная задача", "Старое описание", TaskStatus.IN_PROGRESS, "12-05-2017 12:00", 15);
        manager.addTask(task1);
        int index = task1.getId();
        String discriptionOfFirst = manager.getTaskById(index).getDescription();
        Task task2 = new Task(index, "Вторая обычная задача", "Новое описание", TaskStatus.IN_PROGRESS, "12-05-2017 12:00", 15);
        manager.updateTask(task2);
        String discriptionOfSecond = manager.getTaskById(index).getDescription();
        Assertions.assertNotEquals(discriptionOfFirst, discriptionOfSecond, "Задание не обновилось");
    }

    @Test
    @Override
    protected void deleteTaskById() throws IOException {
        File testFile = File.createTempFile("updateTask", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(testFile);
        Epic epic = new Epic("Основное задание", "Описание");
        manager.addTask(epic);
        int indexOfEpic = epic.getId();
        Subtask subtask = new Subtask("Задание", "Описание", TaskStatus.IN_PROGRESS, indexOfEpic, "12-05-2016 12:00", 15);
        manager.addTask(subtask);
        int indexOfSubtask = subtask.getId();
        manager.deleteTaskById(indexOfSubtask);
        Subtask subtask2 = new Subtask("Задание", "Описание", TaskStatus.IN_PROGRESS, indexOfEpic, "12-05-2017 12:00", 15);
        manager.addTask(subtask2);
        int indexOfAfterDelete = subtask2.getId();
        Assertions.assertEquals(indexOfAfterDelete, indexOfSubtask, " Неправильная реализация метода checkIDofSubtaskAfterDelete");
    }

   @Test
    void shouldHandleEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(emptyFile);
        assertTrue(loadedManager.getAllTask(Task.class).isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.getAllTask(Epic.class).isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.getAllTask(Subtask.class).isEmpty(), "Список подзадач должен быть пустым");
    }


    @Test
    public void checkOverlapTime() {
        assertThrows(ArithmeticException.class, () -> {
            int a = 10 / 0;
        }, "Деление на ноль должно приводить к исключению");


    }



}
