package com.example.pomodoro_timer.data.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.DateTaskCount;
import com.example.pomodoro_timer.model.TaskModel;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(TaskModel task);

    @Update
    int update(TaskModel task);

    @Delete
    int delete(TaskModel task);

    @Query("SELECT * FROM tasks WHERE user_Id = :userId AND is_Completed = 0 ORDER BY position ASC")
    List<TaskModel> getAll(int userId);

    @Update
    void updateTasks(List<TaskModel> tasks);

    @Query("SELECT * FROM tasks WHERE user_Id = :userId AND id = :taskId")
    TaskModel getTaskById(int userId, int taskId);

    @Query("SELECT * FROM tasks WHERE user_Id = :userId AND category_Id = :categoryId")
    List<TaskModel> getAllTaskByCategory(int userId, int categoryId);

    @Query("SELECT * FROM tasks WHERE user_Id = :userId AND is_Completed = 1")
    List<TaskModel> getAllCompletedTasks(int userId);

    @Query("SELECT * FROM tasks WHERE user_Id = :userId AND is_Completed = 0")
    List<TaskModel> getAllOngoingTasks(int userId);

    @Query("SELECT * FROM tasks WHERE user_Id = :userId AND is_Completed = 1 AND category_Id = :categoryId")
    List<TaskModel> getAllCompletedTasksByCategory(int userId, int categoryId);

    @Query("UPDATE tasks SET category_Id = 0 WHERE user_Id = :userId AND category_Id = :categoryId AND id = :taskId")
    void removeCategoryOfTask(int userId, int categoryId, int taskId);

    @Query("SELECT DATE(time_finished / 1000, 'unixepoch') AS date, COUNT(*) AS completedCount " +
            "FROM tasks " +
            "WHERE is_completed = 1 AND user_id = :userId " +
            "GROUP BY DATE(time_finished / 1000, 'unixepoch')")
    LiveData<List<DateTaskCount>> observeCompletedTaskCountsByDate(int userId);

    @Query("SELECT DATE(time_finished / 1000, 'unixepoch') AS date, COUNT(*) AS completedCount " +
            "FROM tasks " +
            "WHERE is_completed = 1 AND user_id = :userId " +
            "AND time_finished >= :startTime AND time_finished <= :endTime " +
            "GROUP BY DATE(time_finished / 1000, 'unixepoch')")
    LiveData<List<DateTaskCount>> observeCompletedTaskCountsByDateRange(int userId, long startTime, long endTime);

    //@Query("SELECT * FROM TaskModel")
    //public TaskModel[] loadAllUsers();
}
