package com.yandex.hw.server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.server.BaseHttpHandler;
import com.yandex.hw.service.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;


public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

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
            ArrayList<Subtask> subtasks = taskManager.getAllTask(Subtask.class);
            if (subtasks.isEmpty()) {
                sendNotFound(exchange, "Список подзадач пуст");
            } else {
                sendText(exchange, subtasks);
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
            boolean added = taskManager.addTask(subtask);
            if (!added) {
                sendHasInteractions(exchange);
                return;
            }
            sendTextNewTask(exchange);
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
                sendNotFound(exchange, "Подзадача с id " + subtaskId + " не найдена");
                return;
            }
            sendText(exchange, subtask.get());
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
            sendText(exchange, "Успешно удалили задачу");
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
