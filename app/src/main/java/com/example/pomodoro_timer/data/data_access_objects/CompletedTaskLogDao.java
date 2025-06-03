package com.example.pomodoro_timer.data.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.CompletedTaskLogModel;
import com.example.pomodoro_timer.model.DateTaskCount;

import java.util.List;

@Dao
public interface CompletedTaskLogDao {

    @Insert
    long insert(CompletedTaskLogModel completedTaskLog);

    @Update
    void update(CompletedTaskLogModel completedTaskLog);

    @Query("SELECT * FROM completed_task_logs WHERE user_id = :userId AND completion_date = :date")
    CompletedTaskLogModel getLogByUserAndDate(int userId, String date);

    @Query("SELECT completion_date AS date, completed_count AS completedCount " +
            "FROM completed_task_logs " +
            "WHERE user_id = :userId " +
            "ORDER BY completion_date ASC")
    LiveData<List<DateTaskCount>> observeCompletedTaskCountsByDate(int userId);

    @Query("SELECT completion_date AS date, completed_count AS completedCount " +
            "FROM completed_task_logs " +
            "WHERE user_id = :userId " +
            "AND timestamp >= :startTime AND timestamp <= :endTime " +
            "ORDER BY completion_date ASC")
    LiveData<List<DateTaskCount>> observeCompletedTaskCountsByDateRange(int userId, long startTime, long endTime);

    @Query("DELETE FROM completed_task_logs WHERE user_id = :userId")
    void deleteAllLogsForUser(int userId);

    @Query("SELECT SUM(completed_count) FROM completed_task_logs WHERE user_id = :userId")
    int getTotalCompletedTasksForUser(int userId);
}