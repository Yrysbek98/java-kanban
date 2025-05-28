package manager.history;

import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void getHistory() {
        taskManager.addTask(new Epic("Третья задача", "Описание"));
        taskManager.addTask(new Epic("Четвертая задача", "Описание"));
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);

        String name = taskManager.getTaskById(4).getName();
        String actualName = taskManager.getHistory().get(9).getName();
        Assertions.assertEquals(name, actualName, "Неверная реализация метода getHistory()");

    }
}