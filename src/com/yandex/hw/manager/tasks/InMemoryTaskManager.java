package com.yandex.hw.manager.tasks;

import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.history.HistoryManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int idCounter = 1;


    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public <T extends Task> void addTask(T task) {
        if (task instanceof Epic epic) {
            epic.setId(getNewId());
            epics.put(epic.getId(), epic);
        } else if (task instanceof Subtask subtask) {
            subtask.setId(getNewId());
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> epicSubtaskIds = epic.getSubtasks();
            if (epicSubtaskIds == null) {
                epicSubtaskIds = new ArrayList<>();
            }
            epicSubtaskIds.add(subtask.getId());
            epic.setSubtasks(epicSubtaskIds);

            subtasks.put(subtask.getId(), subtask);
            checkStatusOfEpic(epic);
        } else {
            task.setId(getNewId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public <T extends Task> void updateTask(T task) {
        if (task instanceof Epic epic) {
            ArrayList<Integer> subtasksForEpic = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicId() == epic.getId()) {
                    subtasksForEpic.add(subtask.getId());
                }
            }
            epic.setSubtasks(subtasksForEpic);
            epics.put(epic.getId(), epic);
            checkStatusOfEpic(epic);
        } else if (task instanceof Subtask subtask) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> epicSubtaskIds = epic.getSubtasks();
            if (epicSubtaskIds == null) {
                epicSubtaskIds = new ArrayList<>();
            }
            epicSubtaskIds.add(subtask.getId());
            checkStatusOfEpic(epic);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (epics.containsKey(id)) {
            Task task = epics.get(id);
            historyManager.add(task);
            return task;
        } else if (subtasks.containsKey(id)) {
            Task task = subtasks.get(id);
            historyManager.add(task);
            return task;
        } else if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public ArrayList<Task> getAllTask(String type) {
        return switch (type) {
            case "Epic" -> new ArrayList<>(epics.values());
            case "Subtask" -> new ArrayList<>(subtasks.values());
            case "Task" -> new ArrayList<>(tasks.values());
            default -> null;
        };
    }


    @Override
    public void deleteAllTasks(String type) {
        if (type.equals("Task")) {
            tasks.clear();
        } else if (type.equals("Epic")) {
            epics.clear();
            subtasks.clear();
        } else if (type.equals("Subtask")) {
            deleteSubtask();
        }

    }

    @Override
    public ArrayList<Integer> getSubtasks(int id) {
       ArrayList<Integer> subtasksOfEpic = epics.get(id).getSubtasks();
        return new ArrayList<>(subtasksOfEpic);
    }

    @Override
    public Task deleteTaskById(int id) {
        if (epics.containsKey(id)) {
            idCounter--;
            return epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> epicSubtaskIds = epic.getSubtasks();
            int index = epicSubtaskIds.indexOf(id);
            epicSubtaskIds.remove(index);
            epic.setSubtasks(epicSubtaskIds);
            checkStatusOfEpic(epic);
            subtasks.remove(id);
            idCounter--;
        } else if (tasks.containsKey(id)) {
            idCounter--;
            return tasks.remove(id);
        }
        return null;
    }

    @Override
    public ArrayList<Task> getHistory() {

        ArrayList<Task> history = historyManager.getHistory();
        return new ArrayList<>(history);
    }

    @Override
    public void remove(int id) {
        historyManager.remove(id);
    }

    public ArrayList<Subtask> getSubtaskOfEpic(Integer epicId) {
        ArrayList<Subtask> subtask = new ArrayList<>();
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasks();
        for (Integer key : subtasksId) {
            subtask.add(subtasks.get(key));
        }
        return subtask;
    }

    private void deleteSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            ArrayList<Integer> epicSubtaskIds = new ArrayList<>();
            epic.setSubtasks(epicSubtaskIds);
            epic.setTaskStatus(TaskStatus.NEW);
        }
    }

    private static int getNewId() {
        return idCounter++;
    }

    private void checkStatusOfEpic(Epic epic) {
        ArrayList<Integer> subtasksId = epic.getSubtasks();
        if (subtasksId.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Integer key : subtasksId) {
            Subtask subtask = subtasks.get(key);
            TaskStatus status = subtask.getTaskStatus();
            if (status != TaskStatus.NEW) {
                allNew = false;
            }
            if (status != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }


}
