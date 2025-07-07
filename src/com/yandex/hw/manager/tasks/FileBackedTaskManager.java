package com.yandex.hw.manager.tasks;
import com.yandex.hw.model.Task;
import com.yandex.hw.service.CSVFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager{
    private File file;
    public FileBackedTaskManager(File file){
        this.file = file;
    }
    public static FileBackedTaskManager loadFile(File file){
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        return taskManager;
    }





    @Override
    public <T extends Task>  void addTask(T task) {
        super.addTask(task);
        save();
    }
    @Override
    public <T extends Task>  void updateTask(T task) {
        super.updateTask(task);
        save();

    }
    @Override
    public ArrayList<Task> getAllTask(String type) {
        super.getAllTask(type);
        save();
        return null;
    }
    @Override
    public  Task getTaskById(int id) {
        super.getTaskById(id);
        save();
        return  null;
    }
    @Override
    public   Task deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
        return null;
    }
    @Override
    public void deleteAllTasks(String type){
        super.deleteAllTasks(type);
        save();
    }
    @Override
    public ArrayList<Integer> getSubtasks(int id) {
        super.getSubtasks(id);
        save();
        return  null;
    }

    private void save(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            // пишем таски
            for (Task task : getAllTask("Task")){
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }
            for (Task epic : getAllTask("Epic")){
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }
            for (Task subtask : getAllTask("Subtask")){
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            // throw new

        }
    }


}
