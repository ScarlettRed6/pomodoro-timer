package com.example.pomodoro_timer.data.data_access_objects;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM tasks WHERE user_Id = :userId ORDER BY position ASC")
    List<TaskModel> getAll(int userId);

    @Update
    void updateTasks(List<TaskModel> tasks);

    //@Query("SELECT * FROM TaskModel")
    //public TaskModel[] loadAllUsers();
}
