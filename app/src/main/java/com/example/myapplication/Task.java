package com.example.myapplication;

import java.util.Locale;

/**
 * Clase Task
 * Representa una Task
 *
 * @author <a href=erick6aelgg@ciencias.unam.mx> Erick Gael García Gutiérrez - @erick6aelgg </>
 */
public class Task {
    private int taskId;
    private String title;
    private String description;
    private boolean completed;

    // Método constructor
    public Task(int taskId, String title, String description, boolean completed){
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters y Setters
    public int getTaskId() {
        return this.taskId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getCompleted() {
        return isCompleted();
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Método equals
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        Task task = (Task) obj;

        return this.taskId == task.getTaskId() && this.title.equals(task.getTitle()) && this.description.equals(task.getDescription()) && this.completed == task.isCompleted();
    }

    // Método toString
    @Override
    public String toString() {
        return String.format(Locale.US,"{%d, '%s', '%s', %b}",
                this.taskId, this.title, this.description, this.completed);
    }

}

