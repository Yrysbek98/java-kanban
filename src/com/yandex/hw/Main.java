package com.yandex.hw;


import com.yandex.hw.manager.tasks.InMemoryTaskManager;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(new Epic("Первая задача", "Описание"));
        taskManager.addTask((new Epic("Новая обычная задача", "Описание")));
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        System.out.println(taskManager.getAllTask(Epic.class));
        int numberOfTasks = taskManager.getAllTask(Epic.class).size();
        int actualSizeOfArray = taskManager.getHistory().size();
        System.out.println( taskManager.getHistory());

    }
}
