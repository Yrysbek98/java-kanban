import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private Integer generateId = 1;
    private Integer generateIdForEpic = 1;
    private Integer generateIdForSubtask = 1;

    public void addNewTask(Task task) {
        task.setId(getNewId());
        tasks.put(task.getId(), task);
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return (ArrayList<Task>) tasks.values();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task deleteTaskById(Integer id) {
        return tasks.remove(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void addNewEpic(Epic epic) {
        epic.setEpicId(getNewIdForEpic());
        epics.put(epic.getEpicId(), epic);
    }

    public Epic getEpicById(Integer epicId) {
        return epics.get(epicId);
    }

    public ArrayList<Epic> getAllEpicks() {
        return (ArrayList<Epic>) epics.values();
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getEpicId(), epic);
    }

    public Epic deleteEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        epic.getSubtasks().clear();
        return epics.remove(epicId);
    }

    public void deleteAllEpic() {
        epics.clear();
        deleteAllSubtask();

    }

    public void addNewSubtask(Subtask subtask) {
        subtask.setSubtaskId(getNewIdForSubtask());
        subtasks.put(subtask.getSubtaskId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasks().add(subtasks.put(subtask.getSubtaskId(), subtask));
        ArrayList<Subtask> allSubtask = epic.getSubtasks();
        checkStatusOfEpic(epic, allSubtask);

    }

    public Subtask getSubtaskById(Integer subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return (ArrayList<Subtask>) subtasks.values();
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Subtask> allSubtask = epic.getSubtasks();
        allSubtask.set((subtask.getSubtaskId() - 1), subtask);
        checkStatusOfEpic(epic, allSubtask);
        subtasks.put(subtask.getSubtaskId(), subtask);
    }

    public void deleteSubtaskById(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasks().remove(subtask);
        checkStatusOfEpic(epic, epic.getSubtasks());
        subtasks.remove(subtaskId);
    }

    public ArrayList<Subtask> getSubtaskOfEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        return epic.getSubtasks();
    }

    public void deleteAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setTaskStatus(TaskStatus.NEW);
        }
    }

    private void checkStatusOfEpic(Epic epic, ArrayList<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : subtasks) {
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

    private Integer getNewId() {
        return generateId++;
    }

    private Integer getNewIdForSubtask() {
        return generateIdForSubtask++;
    }

    private Integer getNewIdForEpic() {
        return generateIdForEpic++;
    }

}
