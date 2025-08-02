package com.yandex.hw.server;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BaseHttpHandler {
    private static final Gson gson = new Gson(); // или new GsonBuilder().setPrettyPrinting().create();
    private static final java.nio.charset.Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected void sendText(HttpExchange h, int statusCode, String message) throws IOException {
        String json = gson.toJson(Map.of("error", message));
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);

        h.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);

        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }
    protected void sendTextNewTask(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
    protected void sendNotFound(HttpExchange h, String message) throws IOException {
        String json = "{\"error\":\"" + message + "\"}";
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);

        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);

        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }
    protected void sendHasInteractions(HttpExchange h, String message) throws IOException {
        String json = "{\"error\":\"" + message + "\"}";
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);

        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);

        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }

    }
    protected void sendServerError(HttpExchange h) throws IOException {
        String json = "{\"error\":\"" + "Ошибка при обработке запроса" + "\"}";
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);

        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(501, resp.length);

        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }

    }

}