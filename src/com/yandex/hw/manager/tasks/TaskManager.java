package com.yandex.hw.manager.tasks;

import com.yandex.hw.model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public interface TaskManager {
    <T extends Task> boolean addTask(T task);

    <T extends Task> void updateTask(T task);

    Task getTaskById(int id);

    <T extends Task> ArrayList<T> getAllTask(Class<T> taskClass);

    Task deleteTaskById(int id);

    void deleteAllTasks(String type);

    ArrayList<Integer> getSubtasks(int id);

    ArrayList<Task> getHistory();

    void remove(int id);

    <T extends Task> LocalDateTime getEndTime(T task);

    TreeSet<Task> getPrioritizedTasks();

}
