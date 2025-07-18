package com.yandex.hw.manager;

import com.yandex.hw.manager.history.HistoryManager;
import com.yandex.hw.manager.history.InMemoryHistoryManager;
import com.yandex.hw.manager.tasks.FileBackedTaskManager;
import com.yandex.hw.manager.tasks.TaskManager;
import com.yandex.hw.manager.tasks.InMemoryTaskManager;

import java.io.File;


public class Managers {
    public static TaskManager getDefaultTaskManager(File file) {
        return new FileBackedTaskManager(file);
    }

    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
