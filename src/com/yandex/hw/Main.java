package com.yandex.hw;


import com.yandex.hw.manager.tasks.InMemoryTaskManager;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(new Task("1", "asdadsad", TaskStatus.NEW));
        taskManager.addTask(new Task("2", "asdadsad", TaskStatus.NEW));
        taskManager.addTask(new Task("3", "asdadsad", TaskStatus.NEW));
        taskManager.addTask(new Epic("Epic", "dadsads"));
        taskManager.addTask(new Subtask("subtask", "sadsdads", TaskStatus.IN_PROGRESS, 4));
        taskManager.addTask(new Subtask("second..", "sdfsdfsdf", TaskStatus.NEW, 4));

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(3));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(4));
        System.out.println(taskManager.getTaskById(5));
        System.out.println(taskManager.getTaskById(3));
        System.out.println(taskManager.getTaskById(6));
        taskManager.deleteTaskById(6);
        System.out.println(taskManager.getTaskById(4));



        System.out.println(taskManager.getHistory());

    }
}
