package com.yandex.hw;


import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        File file = new File("data.csv");
        TaskManager taskManager = Managers.getDefaultTaskManager(file);
        Task task = new Task("dadsdas", "eqwqewqw", TaskStatus.IN_PROGRESS, "12-05-2017 12:00", 15);
        taskManager.addTask(task);

    }
}
