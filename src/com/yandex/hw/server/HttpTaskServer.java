package com.yandex.hw.server;


import com.sun.net.httpserver.HttpServer;
import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Task;
import com.yandex.hw.server.handles.TasksHandler;
import com.yandex.hw.service.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        File file = new File("third.csv");
        TaskManager taskManager = Managers.getDefaultTaskManager(file);
   //     taskManager.addTask(new Task("123", "123", TaskStatus.DONE, "12-05-2012 12:00", 30));
   //     taskManager.addTask(new Task("222", "123", TaskStatus.DONE, "12-05-2014 12:00", 30));
   //     taskManager.addTask(new Task("333", "123", TaskStatus.DONE, "12-05-2013 12:00", 30));
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.start();
        System.out.println(taskManager.getAllTask(Task.class));

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");


    }

}

