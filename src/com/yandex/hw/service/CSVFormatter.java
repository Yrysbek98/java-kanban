package com.yandex.hw.service;

import com.yandex.hw.manager.history.HistoryManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;


public class CSVFormatter {
    public static String getHeader() {
        return "id, TYPE, name, status, description, epicId";
    }

    public static String toString(Task task) {
        TasksType type;
        if (task instanceof Epic) {
            type = TasksType.EPIC;
            return String.join(",",
                    String.valueOf(task.getId()),
                    String.valueOf(type),
                    task.getName(),
                    task.getDescription(),
                    String.valueOf(task.getTaskStatus())
            );
        } else if (task instanceof Subtask) {
            type = TasksType.SUBTASK;
            return String.join(",",
                    String.valueOf(task.getId()),
                    String.valueOf(type),
                    task.getName(),
                    task.getDescription(),
                    String.valueOf(task.getTaskStatus()),
                    String.valueOf(((Subtask) task).getEpicId())
            );
        }
        type = TasksType.TASK;
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(type),
                task.getName(),
                task.getDescription(),
                String.valueOf(task.getTaskStatus())
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
            return new Epic(id, name, description);
        } else if (type == TasksType.SUBTASK) {
            return new Subtask(id, name, description, status, epicId);
        }
        return new Task(id, name, description, status);
    }


}

