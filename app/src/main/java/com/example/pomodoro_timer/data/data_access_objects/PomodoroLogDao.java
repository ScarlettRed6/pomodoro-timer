package com.example.pomodoro_timer.data.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.PomodoroLogModel;

import java.util.List;

@Dao
public interface PomodoroLogDao {

    //Fields
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PomodoroLogModel pomodoroLog);

    @Update
    void update(PomodoroLogModel pomodoroLog);

    @Query("SELECT * FROM pomodoro_logs WHERE user_id = :userId AND timestamp = :timestamp LIMIT 1")
    LiveData<PomodoroLogModel> getLogForDate(int userId, long timestamp);

    @Query("SELECT * FROM pomodoro_logs WHERE user_id = :userId ORDER BY timestamp ASC")
    LiveData<List<PomodoroLogModel>> getAllLogsForUser(int userId);

    @Query("SELECT * FROM pomodoro_logs WHERE user_id = :userId AND timestamp BETWEEN :start AND :end")
    LiveData<List<PomodoroLogModel>> getLogsBetween(int userId, long start, long end);

    @Query("SELECT * FROM pomodoro_logs WHERE user_id = :userId AND date(timestamp / 1000, 'unixepoch') = date(:timestamp / 1000, 'unixepoch') LIMIT 1")
    PomodoroLogModel getLogForUserAndDate(int userId, long timestamp);

}
