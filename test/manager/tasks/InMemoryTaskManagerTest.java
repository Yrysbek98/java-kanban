package manager.tasks;

import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    @Test
    void addTask() {
        taskManager.addTask(new Task("Обычная задача", "Описание", TaskStatus.IN_PROGRESS));
        taskManager.addTask(new Epic("Первая большая задача", "Описание"));
        taskManager.addTask(new Epic("Вторая основаня задача", "Описание"));
        int sizeOfTasks = taskManager.getAllTask("Task").size();
        int sizeOfEpicTasks = taskManager.getAllTask("Epic").size();
        Assertions.assertNotEquals(sizeOfTasks, sizeOfEpicTasks, "У каждого типа задачи должен быть свой отдельный список");

    }

    @Test
    void updateTask() {
        Task task1 = new Task("Вторая обычная задача", "Старое описание", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        String discriptionOfFirst = taskManager.getTaskById(4).getDescription();
        Task task2 = new Task(4, "Вторая обычная задача", "Новое описание", TaskStatus.IN_PROGRESS);
       taskManager.updateTask(task2);
        String discriptionOfSecond = taskManager.getTaskById(4).getDescription();
        Assertions.assertNotEquals(discriptionOfFirst, discriptionOfSecond, "Задание не обновилось");

    }

    @Test
    void getTaskById() {
        taskManager.addTask(new Task("Новое задание 1", "Описание", TaskStatus.NEW));
        taskManager.addTask(new Task("Новое задание 2 ", "Описание", TaskStatus.NEW));
        int index = taskManager.getTaskById(5).getId();

        Assertions.assertEquals(5, index, "Неправильная реализация метода getTaskById()");

    }


}