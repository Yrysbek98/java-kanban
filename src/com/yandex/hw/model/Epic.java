package com.yandex.hw.model;

import com.yandex.hw.service.TaskStatus;
import com.yandex.hw.service.TasksType;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    private Integer epicId;
    private TasksType type = TasksType.EPIC;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, null, 0);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW, null, 0);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(int id, TasksType type, String name, String description, Integer epicId, String startTime, int duration) {
        super(id, name, description, TaskStatus.NEW, null, 0);
        this.type = type;
        this.epicId = epicId;
        this.subtaskIds = new ArrayList<>();
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
