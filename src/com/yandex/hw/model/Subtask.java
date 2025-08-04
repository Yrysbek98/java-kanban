package com.yandex.hw.model;


import com.yandex.hw.service.TaskStatus;
import com.yandex.hw.service.TasksType;

public class Subtask extends Task {
    private Integer epicsId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId, String startTime, int duration) {
        super(name, description, taskStatus, startTime, duration);
        this.epicsId = epicId;
        this.type = TasksType.SUBTASK;
    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus, Integer epicId, String startTime, int duration) {
        super(id, name, description, taskStatus, startTime, duration);
        this.epicsId = epicId;
        this.type = TasksType.SUBTASK;
    }

    public Subtask(int id, TasksType type, String name, String description, TaskStatus taskStatus, Integer epicId, String startTime, int duration) {
        super(id, type, name, description, taskStatus, epicId, startTime, duration);
        this.epicsId = epicId;
    }

    public Integer getEpicId() {
        return epicsId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getTaskStatus() +
                ", epicId=" + epicsId +
                '}';
    }

    public TasksType getType() {
        return type;
    }
}