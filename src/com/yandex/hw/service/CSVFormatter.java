package com.yandex.hw.service;

import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;


public class CSVFormatter {
    public static String getHeader() {
        return "id, TYPE, name, status, description, epicId";
    }

    public static String toString(Task task) {
        if (task instanceof Epic) {
            return String.join(",",
                    String.valueOf(task.getId()),
                    String.valueOf(task.getType()),
                    task.getName(),
                    String.valueOf(task.getTaskStatus()),
                    task.getDescription(),
                    String.valueOf(task.getEpicId() != null ? task.getEpicId() : "")
            );
        } else if (task instanceof Subtask) {
            return String.join(",",
                    String.valueOf(task.getId()),
                    String.valueOf(task.getType()),
                    task.getName(),
                    String.valueOf(task.getTaskStatus()),
                    task.getDescription(),
                    String.valueOf(task.getEpicId())
            );
        }
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(task.getType()),
                task.getName(),
                String.valueOf(task.getTaskStatus()),
                task.getDescription(),
                String.valueOf(task.getEpicId() != null ? task.getEpicId() : "")
        );
    }

    public static Task fromString(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0].trim());
        TasksType type = TasksType.valueOf(parts[1].trim());
        String name = parts[2].trim();
        TaskStatus status = TaskStatus.valueOf(parts[3].trim());
        String description = parts[4].trim();
        int epicId = Integer.parseInt(parts[5].trim());
        if (type == TasksType.EPIC) {
            return new Epic(id, type, name, description, epicId);
        } else if (type == TasksType.SUBTASK) {
            return new Subtask(id, type, name, description, status, epicId);
        }
        return new Task(id, type, name, description, status, epicId);
    }


}

