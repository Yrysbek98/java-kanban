package com.yandex.hw.server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.server.BaseHttpHandler;
import com.yandex.hw.service.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;


public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                break;
            case POST_EPIC:
                handlePostEpic(exchange);
                break;
            case GET_EPIC_ID:
                handleGetEpicById(exchange);
                break;
            case DELETE_EPIC:
                handleDeleteEpic(exchange);
                break;
            case GET_EPIC_SUBTASKS:
                handleGetSubtasksOfEpic(exchange);
                break;
            case UNKNOWN:
                handleStatusUnknown(exchange);
                break;
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        try {
            ArrayList<Epic> getEpics = taskManager.getAllTask(Epic.class);
            if (getEpics.isEmpty()) {
                sendNotFound(exchange, "Список задач пустой");
            } else {
                sendText(exchange, getEpics);
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
            taskManager.addTask(epic);
            sendTextNewTask(exchange);
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int epicId = Integer.parseInt(pathParts[2]);

            Optional<Epic> epic = taskManager.getAllTask(Epic.class).stream()
                    .filter(p -> p.getId() == epicId)
                    .findFirst();

            if (epic.isEmpty()) {
                sendNotFound(exchange, "Подзадача с id " + epicId + " не найдена");
                return;
            }
            sendText(exchange, epic.get());
        } catch (Exception e) {
            sendServerError(exchange);
        }

    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int epicId = Integer.parseInt(pathParts[2]);
            Optional<Epic> epic = taskManager.getAllTask(Epic.class).stream()
                    .filter(p -> p.getId() == epicId)
                    .findFirst();
            if (epic.isEmpty()) {
                sendNotFound(exchange, "Пост с id " + epicId + " не найден");
                return;
            }
            taskManager.deleteTaskById(epicId);
            sendText(exchange, "Успешно удалили задачу");
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetSubtasksOfEpic(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int epicId = Integer.parseInt(pathParts[2]);
            ArrayList<Integer> getEpics = taskManager.getSubtasks(epicId);
            if (getEpics.isEmpty()) {
                sendNotFound(exchange, "Список задач пустой");
            } else {
                String arrayOfSubtask = getEpics.toString();
                sendText(exchange, arrayOfSubtask);
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleStatusUnknown(HttpExchange exchange) throws IOException {
        sendNotFound(exchange, "Сервер не может найти запрошенный ресурс");
    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && "epics".equals(pathParts[1])) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_EPICS;
                case "POST" -> Endpoint.POST_EPIC;
                default -> Endpoint.UNKNOWN;
            };
        } else if (pathParts.length == 3 && "epics".equals(pathParts[1])) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_EPIC_ID;
                case "DELETE" -> Endpoint.DELETE_EPIC;
                default -> Endpoint.UNKNOWN;
            };
        } else if (pathParts.length == 4
                && "epics".equals(pathParts[1])
                && "subtasks".equals(pathParts[3])
                && requestMethod.equals("GET")) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }
        return Endpoint.UNKNOWN;
    }

}
