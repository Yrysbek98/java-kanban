import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private Integer epicId;
    ;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtasks = new ArrayList<>();
    }

    public Epic(Integer epicId, String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.epicId = epicId;
        this.subtasks = new ArrayList<>();
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }


    @Override
    public String toString() {
        return "Epic{" +
                ", epicId=" + epicId +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", taskStatus=" + getTaskStatus() + '\'' +
                "subtasks=" + subtasks +
                '}';
    }
}




