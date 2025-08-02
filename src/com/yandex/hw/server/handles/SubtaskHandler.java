package com.yandex.hw.server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.server.BaseHttpHandler;
import com.yandex.hw.service.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_SUBTASKS:
                handleGetSubtasks(exchange);
                break;
            case POST_SUBTASK:
                handlePostSubtask(exchange);
                break;
            case GET_SUBTASK_ID:
                handleGetSubtaskById(exchange);
                break;
            case DELETE_SUBTASK:
                handleDeleteSubtask(exchange);
                break;
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        try {
            ArrayList<Subtask> getSubtasks = taskManager.getAllTask(Subtask.class);
            if (getSubtasks.isEmpty()) {
                sendNotFound(exchange, "Список задач пустой");
            } else {
                String response = getSubtasks.stream().map(Subtask::toString).collect(Collectors.joining("\n"));
                sendText(exchange, 200, response);
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            taskManager.addTask(subtask);
            sendTextNewTask(exchange, "Задача добавлена");
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int subtaskId = Integer.parseInt(pathParts[2]);
            Optional<Subtask> subtask = taskManager.getAllTask(Subtask.class).stream()
                    .filter(p -> p.getId() == subtaskId)
                    .findFirst();

            if (subtask.isEmpty()) {
                sendNotFound(exchange, "Пост с id " + subtaskId + " не найден");
                return;
            }
            Task getSubtask = subtask.get();
            Gson gson = new Gson();
            String response = gson.toJson(getSubtask);
            sendText(exchange, 200, response);
        } catch (Exception e) {
            sendServerError(exchange);
        }

    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int subtaskId = Integer.parseInt(pathParts[2]);
            Optional<Subtask> subtask = taskManager.getAllTask(Subtask.class).stream()
                    .filter(p -> p.getId() == subtaskId)
                    .findFirst();
            if (subtask.isEmpty()) {
                sendNotFound(exchange, "Пост с id " + subtaskId + " не найден");
                return;
            }
            taskManager.deleteTaskById(subtaskId);
            sendText(exchange, 200, "Успешно удалили задачу");
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && "subtasks".equals(pathParts[1])) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_SUBTASKS;
                case "POST" -> Endpoint.POST_SUBTASK;
                default -> Endpoint.UNKNOWN;
            };
        } else if (pathParts.length == 3 && "subtasks".equals(pathParts[1])) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_SUBTASK_ID;
                case "DELETE" -> Endpoint.DELETE_SUBTASK;
                default -> Endpoint.UNKNOWN;
            };
        }
        return Endpoint.UNKNOWN;
    }

}
