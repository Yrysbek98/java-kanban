package com.yandex.hw.manager.tasks;

import com.yandex.hw.model.Task;

import java.util.ArrayList;

public interface TaskManager {
    <T extends Task> void addTask(T task);

    <T extends Task> void updateTask(T task);

    Task getTaskById(int id);

    <T extends Task> ArrayList<T> getAllTask(Class<T> taskClass);

    Task deleteTaskById(int id);

    void deleteAllTasks(String type);

    ArrayList<Integer> getSubtasks(int id);

    ArrayList<Task> getHistory();

    void remove(int id);

}
