package com.yandex.hw.server.handles;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Task;
import com.yandex.hw.server.BaseHttpHandler;
import com.yandex.hw.service.Endpoint;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case POST_TASK:
                handlePostTask(exchange);
                break;
            case GET_TASK_ID:
                handleGetTaskById(exchange);
                break;
            case DELETE_TASK:
                handleDeleteTask(exchange);
                break;
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        try {
            ArrayList<Task> getTasks = taskManager.getAllTask(Task.class);
            if (getTasks.isEmpty()) {
                sendNotFound(exchange, "Список задач пустой");
            } else {
                sendText(exchange, getTasks);
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            boolean added = taskManager.addTask(task);
            if (!added) {
                sendHasInteractions(exchange);
                return;
            }
            sendTextNewTask(exchange);
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int taskId = Integer.parseInt(pathParts[2]);
            Optional<Task> task = taskManager.getAllTask(Task.class).stream()
                    .filter(p -> p.getId() == taskId)
                    .findFirst();

            if (task.isEmpty()) {
                sendNotFound(exchange, "Пост с id " + taskId + " не найден");
                return;
            }
            Task getTask = task.get();
            sendText(exchange, getTask);
        } catch (Exception e) {
            sendServerError(exchange);
        }

    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int taskId = Integer.parseInt(pathParts[2]);
            Optional<Task> task = taskManager.getAllTask(Task.class).stream()
                    .filter(p -> p.getId() == taskId)
                    .findFirst();
            if (task.isEmpty()) {
                sendNotFound(exchange, "Пост с id " + taskId + " не найден");
                return;
            }
            taskManager.deleteTaskById(taskId);
            sendText(exchange, "Успешно удалили задачу");
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && "tasks".equals(pathParts[1])) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_TASKS;
                case "POST" -> Endpoint.POST_TASK;
                default -> Endpoint.UNKNOWN;
            };
        } else if (pathParts.length == 3 && "tasks".equals(pathParts[1])) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_TASK_ID;
                case "DELETE" -> Endpoint.DELETE_TASK;
                default -> Endpoint.UNKNOWN;
            };
        }
        return Endpoint.UNKNOWN;
    }

}
