package com.yandex.hw.model;

import com.yandex.hw.service.TaskStatus;
import com.yandex.hw.service.TasksType;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private TasksType type = TasksType.TASK;
    private Integer epicId;
    private String startTime;
    private int duration;

    public Task(String name, String description, TaskStatus taskStatus, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer id, String name, String description, TaskStatus taskStatus, String startTime, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer id, TasksType type, String name, String description, TaskStatus taskStatus, Integer epicId, String startTime, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.type = type;
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus + '\'' +
                ", epicId=" + epicId + '\'' +
                ", startTime=" + startTime + '\'' +
                ", duration=" + duration +
                '}';
    }

    public TasksType getType() {
        return type;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
