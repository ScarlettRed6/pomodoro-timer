package com.example.pomodoro_timer.data.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.data.data_access_objects.UserPreferencesDao;
import com.example.pomodoro_timer.model.UserPreferencesModel;

@Dao
public interface UserPreferencesDao {

    @Insert
    long insert(UserPreferencesModel userPreferences);

    @Update
    int update(UserPreferencesModel userPreferences);

    @Delete
    int delete(UserPreferencesModel userPreferences);

    @Query("SELECT * FROM user_preferences")
    UserPreferencesModel getAllUserPreferences();
}
