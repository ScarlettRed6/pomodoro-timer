package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "completed_task_logs")
public class CompletedTaskLogModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "completion_date")
    private String completionDate; // Format: "YYYY-MM-DD"

    @ColumnInfo(name = "completed_count")
    private int completedCount;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public CompletedTaskLogModel(int userId, String completionDate, int completedCount, long timestamp) {
        this.userId = userId;
        this.completionDate = completionDate;
        this.completedCount = completedCount;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CompletedTaskLogModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", completionDate='" + completionDate + '\'' +
                ", completedCount=" + completedCount +
                ", timestamp=" + timestamp +
                '}';
    }
}