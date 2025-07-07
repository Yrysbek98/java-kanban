package com.yandex.hw.model;

import com.yandex.hw.service.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
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
}
