package com.example.pomodoro_timer.model;

public class DateTaskCount {
    public String date;
    public int completedCount;

    public DateTaskCount(String date, int completedCount) {
        this.date = date;
        this.completedCount = completedCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
