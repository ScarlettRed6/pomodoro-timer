package com.example.pomodoro_timer.data.data_access_objects;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.TaskModel;

@Dao
public interface TaskDao {

    @Insert(onConflict = androidx.room.OnConflictStrategy.ABORT)
    int insert(TaskModel task);

    @Update
    int update(TaskModel task);

    @Delete
    int delete(TaskModel task);

    //@Query("SELECT * FROM TaskModel")
    //public TaskModel[] loadAllUsers();
}
