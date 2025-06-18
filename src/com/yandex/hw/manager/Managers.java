package com.yandex.hw.manager;
import com.yandex.hw.manager.history.HistoryManager;
import com.yandex.hw.manager.history.InMemoryHistoryManager;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.manager.tasks.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefaultTaskManager(){
        return   new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistoryManager(){
        return   new InMemoryHistoryManager();
    }
}
