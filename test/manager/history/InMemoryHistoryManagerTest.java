package manager.history;


import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.history.HistoryManager;
import com.yandex.hw.manager.tasks.InMemoryTaskManager;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import manager.TaskManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

class InMemoryHistoryManagerTest extends TaskManagerTest {
    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    @Override
    protected void addTask() {
        taskManager.addTask(new Epic("Первая задача", "Описание"));
        taskManager.addTask((new Epic("Новая обычная задача", "Описание")));
        taskManager.getTaskById(1);
        int numberOfTasks = taskManager.getAllTask(Epic.class).size();
        int actualSizeOfArray = taskManager.getHistory().size();
        Assertions.assertNotEquals(numberOfTasks, actualSizeOfArray, "Неверная реализация метода add()");
    }

    @Test
    @Override
    protected void updateTask() {
        taskManager.addTask(new Epic("Первая задача", "Описание"));
        taskManager.getTaskById(1);
        taskManager.updateTask(new Epic(1, "Новая задача", "Описание"));
        int numberOfTasks = taskManager.getAllTask(Epic.class).size();
        int actualSizeOfArray = taskManager.getHistory().size();
        Assertions.assertNotEquals(numberOfTasks, actualSizeOfArray, "Неверная реализация метода add()");
    }

    @Test
    @Override
    protected void deleteTaskById() {
        taskManager.addTask(new Epic("Первая задача", "Описание"));
        taskManager.addTask((new Epic("Новая обычная задача", "Описание")));
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.remove(2);
        int numberOfTasks = taskManager.getAllTask(Epic.class).size();
        int actualSizeOfArray = taskManager.getHistory().size();
        Assertions.assertNotEquals(numberOfTasks, actualSizeOfArray, "Неверная реализация метода removeHistory");
    }


    @Test
    void checkDuplicate() {
        ArrayList<Integer> countsOfGetTasks = new ArrayList<>();
        Task task1 = new Task("Задание 1", "Описание", TaskStatus.NEW, "12-05-2017 12:00", 15);
        taskManager.addTask(task1);
        int indexOfTask1 = task1.getId();
        Task task2 = new Task("Задание 2", "Описание", TaskStatus.IN_PROGRESS, "12-04-2017 12:00", 15);
        taskManager.addTask(task1);
        int indexOfTask2 = task2.getId();
        Task task3 = new Task("Задание 3", "Описание", TaskStatus.DONE, "12-06-2017 12:00", 15);
        taskManager.addTask(task1);
        int indexOfTask3 = task3.getId();
        taskManager.getTaskById(indexOfTask1);
        countsOfGetTasks.add(indexOfTask1);
        taskManager.getTaskById(indexOfTask2);
        countsOfGetTasks.add(indexOfTask2);
        taskManager.getTaskById(indexOfTask1);
        countsOfGetTasks.add(indexOfTask1);
        taskManager.getTaskById(indexOfTask3);
        countsOfGetTasks.add(indexOfTask3);
        int sizeOfListOfGetTasks = countsOfGetTasks.size();
        int sizeOfHistoryList = taskManager.getHistory().size();
        Assertions.assertNotEquals(sizeOfListOfGetTasks, sizeOfHistoryList, "Неверная реализация метода checkDuplicate()");

    }
}