package com.yandex.hw.server.handles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Task;
import com.yandex.hw.server.BaseHttpHandler;
import com.yandex.hw.service.Endpoint;

import java.io.IOException;
import java.util.TreeSet;

public class PriorityHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PriorityHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint == Endpoint.GET_PRIORITIZED) {
            handleGetPrioritized(exchange);
        } else {
            handleStatusUnknown(exchange);
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        try {
            TreeSet<Task> getTasks = taskManager.getPrioritizedTasks();
            if (getTasks.isEmpty()) {
                sendNotFound(exchange, "Список  пустой");
            } else {
                sendText(exchange, getTasks);
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
        if (pathParts.length == 2 && "prioritized".equals(pathParts[1]) && requestMethod.equals("GET")) {
            return Endpoint.GET_PRIORITIZED;

        }
        ;
        return Endpoint.UNKNOWN;
    }
}
