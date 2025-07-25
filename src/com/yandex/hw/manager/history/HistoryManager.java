package com.yandex.hw.manager.history;

import com.yandex.hw.model.Task;
import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistory();

    void remove(int id);
}
