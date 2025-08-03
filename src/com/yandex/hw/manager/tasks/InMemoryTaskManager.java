package com.yandex.hw.manager.tasks;

import com.yandex.hw.manager.Managers;
import com.yandex.hw.manager.history.HistoryManager;
import com.yandex.hw.model.Epic;
import com.yandex.hw.model.Subtask;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected static int idCounter = 1;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    protected final TreeSet<Task> priorityTasks = new TreeSet<>(Comparator.comparing((task -> LocalDateTime.parse(task.getStartTime(), formatter))));

    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public <T extends Task> boolean addTask(T task) {
        if (task == null) {
            throw new IllegalArgumentException("Task не может быть null");
        }
        if (task instanceof Epic epic) {
            if (epic.getSubtasks() == null) {
                epic.setSubtasks(new ArrayList<>());
            }
            epic.setId(getNewId());
            epics.put(epic.getId(), epic);
        } else if (task instanceof Subtask subtask) {
            if (checkOverlapTime(subtask)) {
                //   throw new IllegalArgumentException("Перекрытия по времени");
                return false;
            } else {
                subtask.setId(getNewId());
                Integer epicId = subtask.getEpicId();
                if (epicId == null) {
                    throw new IllegalArgumentException("У Subtask не задан epicId");
                }
                Epic epic = epics.get(subtask.getEpicId());
                if (epic == null) {
                    throw new IllegalArgumentException("Epic с id=" + subtask.getEpicId() + " не найден");
                }
                ArrayList<Integer> epicSubtaskIds = epic.getSubtasks();
                if (epicSubtaskIds == null) {
                    epicSubtaskIds = new ArrayList<>();
                }
                epicSubtaskIds.add(subtask.getId());
                epic.setSubtasks(epicSubtaskIds);

                subtasks.put(subtask.getId(), subtask);
                priorityTasks.add(subtask);
                checkStatusOfEpic(epic);
                updateStartTimeOfEpic(epic);
                updateDurationOfEpic(epic);

            }
        } else {
            if (checkOverlapTime(task)) {
                return false;
            } else {
                task.setId(getNewId());
                tasks.put(task.getId(), task);
                priorityTasks.add(task);
            }

        }
        return true;
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
            updateStartTimeOfEpic(epic);
            updateDurationOfEpic(epic);
        } else if (task instanceof Subtask subtask) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> epicSubtaskIds = epic.getSubtasks();
            if (epicSubtaskIds == null) {
                epicSubtaskIds = new ArrayList<>();
            }
            epicSubtaskIds.add(subtask.getId());
            checkStatusOfEpic(epic);
            updateStartTimeOfEpic(epic);
            updateDurationOfEpic(epic);
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
    public <T extends Task> ArrayList<T> getAllTask(Class<T> taskClass) {
        if (taskClass == Epic.class) {
            return new ArrayList<>((Collection<T>) epics.values());
        } else if (taskClass == Subtask.class) {
            return new ArrayList<>((Collection<T>) subtasks.values());
        } else if (taskClass == Task.class) {
            return new ArrayList<>((Collection<T>) tasks.values());
        } else {
            return null;
        }
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
            return epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> epicSubtaskIds = epic.getSubtasks();
            int index = epicSubtaskIds.indexOf(id);
            epicSubtaskIds.remove(index);
            epic.setSubtasks(epicSubtaskIds);
            checkStatusOfEpic(epic);
            updateStartTimeOfEpic(epic);
            updateDurationOfEpic(epic);
            subtasks.remove(id);
        } else if (tasks.containsKey(id)) {
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return new TreeSet<>(priorityTasks);
    }

    @Override
    public <T extends Task> LocalDateTime getEndTime(T task) {
        if (task instanceof Epic epic) {
            ArrayList<Integer> subtasksId = epic.getSubtasks();
            int durationsOfEpic = 0;
            LocalDateTime latestStartTime = null;

            for (int i = 0; i < subtasksId.size(); i++) {
                Subtask subtask = subtasks.get(subtasksId.get(i));
                durationsOfEpic += subtask.getDuration();

                if (latestStartTime == null || LocalDateTime.parse(subtask.getStartTime(), formatter).isAfter(latestStartTime)) {
                    latestStartTime = LocalDateTime.parse(subtask.getStartTime(), formatter);
                }
            }

            if (latestStartTime != null) {
                return latestStartTime.plusMinutes(durationsOfEpic);
            } else {
                return null;
            }

        }
        return LocalDateTime.parse(task.getStartTime(), formatter).plusMinutes(task.getDuration());
    }

    private void updateStartTimeOfEpic(Epic epic) {
        ArrayList<Integer> subtasksId = epic.getSubtasks();
        LocalDateTime earliestStartTime = null;

        for (int subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            LocalDateTime subtaskStartTime = LocalDateTime.parse(subtask.getStartTime(), formatter);

            if (subtaskStartTime != null &&
                    (earliestStartTime == null || subtaskStartTime.isBefore(earliestStartTime))) {
                earliestStartTime = subtaskStartTime;
            }
        }
        if (earliestStartTime == null) {
            epic.setStartTime(null);
        } else {
            epic.setStartTime(earliestStartTime.format(formatter));
            priorityTasks.add(epic);
        }

    }

    private void updateDurationOfEpic(Epic epic) {
        ArrayList<Integer> subtasksId = epic.getSubtasks();
        int totalMinutes = 0;

        for (int subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            int subtaskDuration = subtask.getDuration();

            if (subtaskDuration != 0) {
                totalMinutes += subtaskDuration;
            }
        }

        epic.setDuration(totalMinutes);
    }

    private <T extends Task> boolean checkOverlapTime(T task) {
        LocalDateTime start = LocalDateTime.parse(task.getStartTime(), formatter);
        LocalDateTime end = getEndTime(task);

        return Stream.concat(
                        Stream.concat(
                                tasks.values().stream(),
                                epics.values().stream()
                        ),
                        subtasks.values().stream()
                )
                .filter(t -> t.getId() != task.getId())
                .filter(t -> {
                    if (task instanceof Subtask subtask && t instanceof Epic epic) {
                        Integer subEpicId = subtask.getEpicId();
                        if (subEpicId == null) return true;
                        return !subEpicId.equals(epic.getId());
                    }
                    if (task instanceof Epic epic && t instanceof Subtask subtask) {
                        Integer subEpicId = subtask.getEpicId();
                        if (subEpicId == null) return true;
                        return !subEpicId.equals(epic.getId());
                    }
                    return true;
                })
                .anyMatch(t -> {
                    String tStartTime = t.getStartTime();
                    if (tStartTime == null) return false;

                    LocalDateTime otherStart = LocalDateTime.parse(tStartTime, formatter);
                    LocalDateTime otherEnd = getEndTime(t);

                    return !(end.isBefore(otherStart) || start.isAfter(otherEnd));
                });
    }

}
