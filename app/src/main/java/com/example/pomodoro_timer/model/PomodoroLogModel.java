package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pomodoro_logs")
public class PomodoroLogModel {

    //Fields
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "session_count")
    private int sessionCount;

    @ColumnInfo(name = "focus_time")
    private long focusTime;

    @ColumnInfo(name = "break_time")
    private long breakTime;

    //Constructor
    public PomodoroLogModel(int userId, long timestamp, int sessionCount, long focusTime) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.sessionCount = sessionCount;
        this.focusTime = focusTime;
    }
    //Constructor for focus and break time
    @Ignore
    public PomodoroLogModel(int userId, long timestamp, long breakTime) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.breakTime = breakTime;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public int getSessionCount() {
        return sessionCount;
    }
    public long getFocusTime() {
        return focusTime;
    }
    public long getBreakTime() {
        return breakTime;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
    public void setFocusTime(long focusTimeMinutes) {
        this.focusTime = focusTimeMinutes;
    }
    public void setBreakTime(long breakTimeMinutes) {
        this.breakTime = breakTimeMinutes;
    }

}
