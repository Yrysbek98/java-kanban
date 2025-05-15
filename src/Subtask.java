public class Subtask extends Task {
    private Integer subtaskId;
    private Integer epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(Integer subtaskId, String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus);
        this.subtaskId = subtaskId;
        this.epicId = epicId;
    }

    public Integer getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId = subtaskId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + subtaskId +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getTaskStatus() +
                ", epicId=" + epicId +
                '}';
    }
}

