package com.example.pomodoro_timer.data.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.StatsModel;

import java.util.List;

@Dao
public interface StatsDao {

    @Insert
    long insert(StatsModel stats);

    @Update
    int update(StatsModel stats);

    @Delete
    int delete(StatsModel stats);

    @Query("SELECT * FROM stats WHERE userId = :userId LIMIT 1")
    LiveData<StatsModel> getStatsByUser(int userId);

    @Query("SELECT * FROM stats WHERE id = :id")
    LiveData<StatsModel> getStatsById(int id);

    @Query("SELECT * FROM stats")
    LiveData<List<StatsModel>> getAllStats();
}
