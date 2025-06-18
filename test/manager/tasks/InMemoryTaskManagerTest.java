package manager.tasks;

import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;




class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    @Test
    void addDifferentTask() {
        taskManager.addTask(new Task("Обычная задача", "Описание", TaskStatus.IN_PROGRESS));
        taskManager.addTask(new Epic("Первая большая задача", "Описание"));
        taskManager.addTask(new Epic("Вторая основаня задача", "Описание"));
        int sizeOfTasks = taskManager.getAllTask("Task").size();
        int sizeOfEpicTasks = taskManager.getAllTask("Epic").size();
        Assertions.assertNotEquals(sizeOfTasks, sizeOfEpicTasks, "У каждого типа задачи должен быть свой отдельный список");
    }

    @Test
    void setAndUpdateTask() {
        Task task1 = new Task("Вторая обычная задача", "Старое описание", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        int index = task1.getId();
        String discriptionOfFirst = taskManager.getTaskById(index).getDescription();
        Task task2 = new Task(index, "Вторая обычная задача", "Новое описание", TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task2);
        String discriptionOfSecond = taskManager.getTaskById(index).getDescription();
        Assertions.assertNotEquals(discriptionOfFirst, discriptionOfSecond, "Задание не обновилось");
    }

    @Test
    void checkSubtaskOfEpic() {
        Epic epic = new Epic("Основное задание", "Описание");
        taskManager.addTask(epic);
        int indexOfEpic = epic.getId();
        Subtask subtask1 = new Subtask("Первое задание", "Описание", TaskStatus.IN_PROGRESS, indexOfEpic);
        taskManager.addTask(subtask1);
        int indexOfFirstSubtask = subtask1.getId();
        Subtask subtask2 = new Subtask("Второе задание", "Описание", TaskStatus.NEW, indexOfEpic);
        taskManager.addTask(subtask2);
        int indexOfSecondSubtask = subtask2.getId();
        int sizeOfSubtasksBeforeDelete = taskManager.getSubtasks(indexOfEpic).size();
        taskManager.deleteTaskById(indexOfFirstSubtask);
        int sizeOfSubtasksAfterDelete = taskManager.getSubtasks(indexOfEpic).size();
        Assertions.assertNotEquals(sizeOfSubtasksBeforeDelete, sizeOfSubtasksAfterDelete, "Неправильная реализация метода checkSubtaskOfEpic");
    }

    @Test
    void checkIDofSubtaskAfterDelete() {
        Epic epic = new Epic("Основное задание", "Описание");
        taskManager.addTask(epic);
        int indexOfEpic = epic.getId();
        Subtask subtask = new Subtask("Задание", "Описание", TaskStatus.IN_PROGRESS, indexOfEpic);
        taskManager.addTask(subtask);
        int indexOfSubtask = subtask.getId();
        taskManager.deleteTaskById(indexOfSubtask);
        Subtask subtask2 = new Subtask("Задание", "Описание", TaskStatus.IN_PROGRESS, indexOfEpic);
        taskManager.addTask(subtask2);
        int indexOfAfterDelete = subtask2.getId();
        Assertions.assertEquals(indexOfAfterDelete, indexOfSubtask, " Неправильная реализация метода checkIDofSubtaskAfterDelete");
    }


    @Test
    void getTaskById() {
        Task task1 = new Task("Новое задание 1", "Описание", TaskStatus.NEW);
        taskManager.addTask(task1);
        int index1 = task1.getId();
        Task task2 = new Task("Новое задание 2", "Описание", TaskStatus.NEW);
        taskManager.addTask(task2);
        int index2 = task2.getId();
        int index = taskManager.getTaskById(index1).getId();
        Assertions.assertEquals(index, index1, "Неправильная реализация метода getTaskById()");

    }
}