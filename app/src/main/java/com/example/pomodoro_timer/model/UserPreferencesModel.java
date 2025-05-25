package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_preferences")
public class UserPreferencesModel {

    //Fields

    @PrimaryKey(autoGenerate = true)
    private int id;

    //Foreign Key
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "theme")
    private int theme;

    @ColumnInfo(name = "alarm")
    private int alarm;

    @ColumnInfo(name = "allow_notifications")
    private boolean allowNotifications;

    @ColumnInfo(name = "allow_nudge")
    private boolean allowNudge;

    @ColumnInfo(name = "duration_pomodoro")
    private long durationPomodoro;

    @ColumnInfo(name = "duration_short_break")
    private long durationShortBreak;

    @ColumnInfo(name = "duration_long_break")
    private long durationLongBreak;

    @ColumnInfo(name = "long_break_interval")
    private int longBreakInterval;

    @ColumnInfo(name = "auto_start_pomodoro")
    private boolean autoStartPomodoro;

    @ColumnInfo(name = "auto_start_breaks")
    private boolean autoStartBreaks;

    public UserPreferencesModel(int userId, int theme, int alarm, boolean allowNotifications, boolean allowNudge,
                                long durationPomodoro, long durationLongBreak, long durationShortBreak,
                                int longBreakInterval, boolean autoStartPomodoro, boolean autoStartBreaks) {
        this.userId = userId;
        this.theme = theme;
        this.alarm = alarm;
        this.allowNotifications = true;
        this.allowNudge = false;
        this.durationPomodoro = durationPomodoro;
        this.durationLongBreak = durationLongBreak;
        this.durationShortBreak = durationShortBreak;
        this.longBreakInterval = longBreakInterval;
        this.autoStartPomodoro = autoStartPomodoro;
        this.autoStartBreaks = autoStartBreaks;
    }
    //Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getTheme() { return theme; }
    public int getAlarm() { return alarm; }
    public boolean getAllowNotifications() { return allowNotifications; }
    public boolean getAllowNudge() { return allowNudge; }
    public long getDurationPomodoro() { return durationPomodoro; }
    public long getDurationLongBreak() { return durationLongBreak; }
    public long getDurationShortBreak() { return durationShortBreak; }
    public long getLongBreakInterval() { return longBreakInterval; }
    public boolean getAutoStartPomodoro() { return autoStartPomodoro; }
    public boolean getAutoStartBreaks() { return autoStartBreaks; }
    //Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void theme(int theme) { this.theme = theme; }
    public void setAlarm(int alarm) { this.alarm = alarm; }
    public void setAllowNotifications(boolean allowNotifications) { this.allowNotifications = allowNotifications; }
    public void setAllowNudge(boolean allowNudge) { this.allowNudge = allowNudge; }
    public void setDurationPomodoro(long durationPomodoro) { this.durationPomodoro = durationPomodoro; }
    public void setDurationLongBreak(long durationLongBreak) { this.durationLongBreak = durationLongBreak; }
    public void setDurationShortBreak(long durationShortBreak) { this.durationShortBreak = durationShortBreak; }
    public void setLongBreakInterval(int longBreakInterval) { this.longBreakInterval = longBreakInterval; }
    public void setAutoStartPomodoro(boolean autoStartPomodoro) { this.autoStartPomodoro = autoStartPomodoro; }
    public void setAutoStartBreaks(boolean autoStartBreaks) { this.autoStartBreaks = autoStartBreaks; }
}
