package com.yandex.hw;


import com.yandex.hw.model.Task;
import com.yandex.hw.service.TaskStatus;

public class Main {

    public static void main(String[] args) {
        Task task = new Task("Задание", "Описание ", TaskStatus.IN_PROGRESS);

    }
}
