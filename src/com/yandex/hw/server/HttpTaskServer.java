package com.yandex.hw.server;


import com.sun.net.httpserver.HttpServer;
import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;

import com.yandex.hw.server.handles.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        File file = new File("third.csv");
        TaskManager taskManager = Managers.getDefaultTaskManager(file);
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));
        httpServer.start();


        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");


    }

}

