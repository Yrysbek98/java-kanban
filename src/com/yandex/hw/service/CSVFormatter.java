package com.yandex.hw.service;

public class CSVFormatter {
    public static String getHeader(){
        return "id, TYPE, name,description, epicId";
    }
    public static <T> String toString(T task){
        return task.toString();
    }
}

