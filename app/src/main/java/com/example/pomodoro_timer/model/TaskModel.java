package com.example.pomodoro_timer.model;

public class TaskModel {
    private String taskTitle;
    private int sessionCount;
    private int priorityLevel; //Levels: 1 = low, 2 = medium, 3 = high

    public TaskModel(String taskTitle, int sessionCount, int priorityLevel){
        this.taskTitle = taskTitle;
        this.sessionCount = sessionCount;
        this.priorityLevel = priorityLevel;
    }

    //Getters and Setters
    public String getTitle(){
        return taskTitle;
    }
    public int getSessionCount(){
        return sessionCount;
    }
    public int getPriorityLevel(){
        return priorityLevel;
    }

}
