package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "task_title")
    private String taskTitle;
    private int sessionCount;
    private int priorityLevel; //Levels: 1 = low, 2 = medium, 3 = high
    private String categoryTitle;
    private String taskDescription;

    public TaskModel(String taskTitle, int sessionCount, int priorityLevel, String categoryTitle, String taskDescription){
        this.taskTitle = taskTitle;
        this.sessionCount = sessionCount;
        this.priorityLevel = priorityLevel;
        this.categoryTitle = categoryTitle;
        this.taskDescription = taskDescription;
    }

    //Getters and Setters
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTaskTitle(){
        return taskTitle;
    }
    public void setTaskTitle(String taskTitle){
        this.taskTitle = taskTitle;
    }
    public int getSessionCount(){
        return sessionCount;
    }
    public int getPriorityLevel(){
        return priorityLevel;
    }
    public String getCategoryTitle(){
        return categoryTitle;
    }
    public String getTaskDescription(){
        return taskDescription;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "title='" + taskTitle + '\'' +
                ", sessionCount=" + sessionCount +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TaskModel task = (TaskModel) obj;
        return taskTitle.equals(task.taskTitle);
    }

    @Override
    public int hashCode() {
        return taskTitle.hashCode();
    }

}
