package com.yandex.hw.model;

import com.yandex.hw.service.TaskStatus;

public class Subtask extends Task {
    // private Integer subtaskId;
    private Integer epicsId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus);
        this.epicsId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(id, name, description, taskStatus);
      //  this.subtaskId = subtaskId;
        this.epicsId = epicId;
    }

   /* public Integer getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId = subtaskId;
    }*/

    public int getEpicId() {
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
}
