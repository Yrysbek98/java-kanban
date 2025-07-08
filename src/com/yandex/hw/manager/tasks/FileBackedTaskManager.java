package com.yandex.hw.manager.tasks;

import com.yandex.hw.exceptions.ManagerSaveException;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.CSVFormatter;
import com.yandex.hw.service.TasksType;

import java.io.*;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }

                Task task = CSVFormatter.fromString(line);
                if (task != null) {

                    if (task.getId() > manager.idCounter) {
                        manager.idCounter = task.getId();
                    }

                    if (task.getType() == TasksType.EPIC) {
                        manager.epics.put(task.getId(), (Epic) task);
                    } else if (task.getType() == TasksType.SUBTASK) {
                        Subtask subtask = (Subtask) task;
                        manager.subtasks.put(subtask.getId(), subtask);

                        Epic epic = manager.epics.get(subtask.getEpicId());
                        if (epic != null) {
                            if (epic.getSubtasks() == null) {
                                epic.setSubtasks(new ArrayList<>());
                            } else {
                                epic.addSubtask(subtask.getId());
                            }
                        }
                    } else {
                        manager.tasks.put(task.getId(), task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }

        return manager;
    }


    @Override
    public <T extends Task> void addTask(T task) {
        try {
            super.addTask(task);
            save();
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при добавлении task", e);
        }
    }

    @Override
    public <T extends Task> void updateTask(T task) {
        super.updateTask(task);
        save();

    }

    @Override
    public <T extends Task> ArrayList<T> getAllTask(Class<T> taskClass) {
        return super.getAllTask(taskClass);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Task deleteTaskById(int id) {
        Task task = super.deleteTaskById(id);
        save();
        return task;
    }

    @Override
    public void deleteAllTasks(String type) {
        super.deleteAllTasks(type);
        save();
    }

    @Override
    public ArrayList<Integer> getSubtasks(int id) {
        ArrayList<Integer> subtasks = super.getSubtasks(id);
        save();
        return subtasks;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            for (Task task : getAllTask(Task.class)) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }
            for (Epic epic : getAllTask(Epic.class)) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getAllTask(Subtask.class)) {
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: ", e);
        }
    }


}
