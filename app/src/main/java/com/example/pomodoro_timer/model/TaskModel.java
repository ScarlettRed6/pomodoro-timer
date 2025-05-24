package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "task_title")
    private String taskTitle;

    @ColumnInfo(name = "session_count")
    private int sessionCount;

    @ColumnInfo(name = "remaining_sessions")
    private int remainingSessions;

    @ColumnInfo(name = "priority_level")
    private int priorityLevel; //Levels: 1 = low, 2 = medium, 3 = high

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @ColumnInfo(name = "time_finished")
    private long timeFinished;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted = false;

    public TaskModel(int userId, String taskTitle, int sessionCount, int priorityLevel, int categoryId, String taskDescription){
        this.userId = userId;
        this.taskTitle = taskTitle;
        this.sessionCount = sessionCount;
        this.remainingSessions = sessionCount;
        this.priorityLevel = priorityLevel;
        this.categoryId = categoryId;
        this.taskDescription = taskDescription;
    }

    //Getters and Setters
    public int getId(){
        return id;
    }
    public int getUserId(){
        return userId;
    }
    public String getTaskTitle(){
        return taskTitle;
    }
    public int getSessionCount(){
        return sessionCount;
    }
    public int getPriorityLevel(){
        return priorityLevel;
    }
    public int getCategoryId(){
        return categoryId;
    }
    public String getTaskDescription(){
        return taskDescription;
    }
    public long getTimeFinished(){
        return timeFinished;
    }
    public int getPosition(){
        return position;
    }
    public boolean getIsCompleted(){
        return isCompleted;
    }
    public int getRemainingSessions() {
        return remainingSessions;
    }

    public void setTaskTitle(String taskTitle){
        this.taskTitle = taskTitle;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public void setSessionCount(int sessionCount){
        this.sessionCount = sessionCount;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPriorityLevel(int priorityLevel){
        this.priorityLevel = priorityLevel;
    }
    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }
    public void setTaskDescription(String taskDescription){
        this.taskDescription = taskDescription;
    }
    public void setTimeFinished(long timeStamp){
        this.timeFinished = timeStamp;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public void setRemainingSessions(int remainingSessions) {
        this.remainingSessions = remainingSessions;
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
