package com.yandex.hw;


import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.FileBackedTaskManager;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;

public class Main {

    public static void main(String[] args) {
        Task task = new Task("Задание", "Описание ", TaskStatus.IN_PROGRESS);
        TaskManager taskManager = Managers.getTaskManager();
        taskManager.addTask(task);
    }
}
