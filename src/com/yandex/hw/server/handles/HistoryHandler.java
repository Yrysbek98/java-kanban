package com.yandex.hw.server.handles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.model.Task;
import com.yandex.hw.server.BaseHttpHandler;
import com.yandex.hw.service.Endpoint;

import java.io.IOException;
import java.util.ArrayList;


public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint == Endpoint.GET_HISTORY) {
            try {
                ArrayList<Task> getTasks = taskManager.getHistory();
                if (getTasks.isEmpty()) {
                    sendNotFound(exchange, "Список историй пустой");
                } else {
                    sendText(exchange, getTasks);
                }
            } catch (Exception e) {
                sendServerError(exchange);
            }
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && "history".equals(pathParts[1]) && requestMethod.equals("GET")) {
            return Endpoint.GET_HISTORY;

        }
        ;
        return Endpoint.UNKNOWN;
    }
}
