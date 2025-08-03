package com.yandex.hw.model;

import com.google.gson.annotations.Expose;
import com.yandex.hw.service.TaskStatus;
import com.yandex.hw.service.TasksType;

import java.util.ArrayList;

public class Epic extends Task {
    @Expose
    private ArrayList<Integer> subtaskIds;
    private transient Integer epicId;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, null, 0);
        this.subtaskIds = new ArrayList<>();
        this.type = TasksType.EPIC;
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW, null, 0);
        this.subtaskIds = new ArrayList<>();
        this.type = TasksType.EPIC;
    }

    public Epic(int id, TasksType type, String name, String description, Integer epicId, String startTime, int duration) {
        super(id, type, name, description, TaskStatus.NEW, epicId, startTime, duration);
        this.epicId = epicId;
        this.subtaskIds = new ArrayList<>();
    }

    public Epic() {
        super("", "", TaskStatus.NEW, null, 0); // значения по умолчанию
        this.subtaskIds = new ArrayList<>();
        this.type = TasksType.EPIC;
    }


    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<Integer>(subtaskIds);
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtaskIds = subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicId=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getTaskStatus() + '\'' +
                "subtasks=" + subtaskIds +
                '}';
    }

    public void addSubtask(int id) {
        subtaskIds.add(id);
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TasksType getType() {
        return type;
    }
}