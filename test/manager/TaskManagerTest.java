package manager;

import com.yandex.hw.manager.tasks.TaskManager;


import java.io.IOException;

public abstract class TaskManagerTest<T extends TaskManager>{

    protected abstract void addTask() throws IOException;

    protected abstract void updateTask() throws IOException;

    protected abstract  void deleteTaskById() throws IOException;

}