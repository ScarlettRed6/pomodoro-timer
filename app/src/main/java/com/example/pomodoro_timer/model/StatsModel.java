package com.example.pomodoro_timer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "stats")
public class StatsModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "productivityScore")
    public int productivityScore;

    @ColumnInfo(name = "totalFocus")
    public int totalFocus;

    @ColumnInfo(name = "breakTime")
    public int breakTime;

    @ColumnInfo(name = "pomodoroSessions")
    public int totalPomodoroSessions;

    @ColumnInfo(name = "userId")
    public int userID;

    //Constructor
    public StatsModel(int productivityScore, int totalFocus, int breakTime, int totalPomodoroSessions) {
        this.productivityScore = productivityScore;
        this.totalFocus = totalFocus;
        this.breakTime = breakTime;
        this.totalPomodoroSessions = totalPomodoroSessions;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getProductivityScore() {
        return productivityScore;
    }
    public void setProductivityScore(int productivityScore) {
        this.productivityScore = productivityScore;
    }
    public int getTotalFocus() {
        return totalFocus;
    }
    public void setTotalFocus(int totalFocus) {
        this.totalFocus = totalFocus;
    }
    public int getBreakTime() {
        return breakTime;
    }
    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }
    public int getTotalPomodoroSessions() {
        return totalPomodoroSessions;
    }
    public void setTotalPomodoroSessions(int totalPomodoroSessions) {
        this.totalPomodoroSessions = totalPomodoroSessions;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

}
