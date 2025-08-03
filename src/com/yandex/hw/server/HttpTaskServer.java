package com.yandex.hw.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.tasks.TaskManager;

import com.yandex.hw.model.Task;
import com.yandex.hw.server.handles.*;
import com.yandex.hw.service.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static Gson gson = new Gson(); // Простой Gson без настроек
    private HttpServer httpServer;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
    }

        public void start() throws IOException {
        if (httpServer != null) {
            throw new IllegalStateException("Сервер уже запущен");
        }

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
            httpServer = null;
            System.out.println("HTTP-сервер остановлен");
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("data.csv");
        TaskManager taskManager = Managers.getDefaultTaskManager(file);
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        HttpTaskServer.gson = gson;
    }
}

