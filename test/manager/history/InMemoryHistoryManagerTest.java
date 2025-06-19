package manager.history;

import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    @Test
    void add() {
        taskManager.addTask(new Epic("Первая задача", "Описание"));
        taskManager.addTask((new Epic("Новая обычная задача", "Описание")));
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        int numberOfTasks = taskManager.getAllTask("Epic").size();
        int actualSizeOfArray = taskManager.getHistory().size();
        Assertions.assertNotEquals(numberOfTasks, actualSizeOfArray, "Неверная реализация метода add()");
    }

    @Test
    void remove() {
        Task task1 = new Task("Задание 1", "Описание", TaskStatus.NEW);
        taskManager.addTask(task1);
        int indexOfTask1 = task1.getId();
        Task task2 = new Task("Задание 2", "Описание", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task2);
        int indexOfTask2 = task2.getId();
        Task task3 = new Task("Задание 3", "Описание", TaskStatus.DONE);
        taskManager.addTask(task3);
        int indexOfTask3 = task3.getId();
        taskManager.getTaskById(indexOfTask1);
        taskManager.getTaskById(indexOfTask2);
        taskManager.getTaskById(indexOfTask3);
        int countOfHistoryList = taskManager.getHistory().size();
        taskManager.remove(indexOfTask2);
        int countAfterRemoveHistory = taskManager.getHistory().size();
        Assertions.assertNotEquals(countOfHistoryList, countAfterRemoveHistory, "Неверная реализация метода remove()");

    }

    @Test
    void checkDuplicate() {
        ArrayList<Integer> countsOfGetTasks = new ArrayList<>();
        Task task1 = new Task("Задание 1", "Описание", TaskStatus.NEW);
        taskManager.addTask(task1);
        int indexOfTask1 = task1.getId();
        Task task2 = new Task("Задание 2", "Описание", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        int indexOfTask2 = task2.getId();
        Task task3 = new Task("Задание 3", "Описание", TaskStatus.DONE);
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