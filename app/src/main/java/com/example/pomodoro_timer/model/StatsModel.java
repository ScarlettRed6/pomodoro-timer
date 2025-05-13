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

    @ColumnInfo(name = "taskID")
    public int taskID; // Foreign Key (assume related constraint handled elsewhere)
}
