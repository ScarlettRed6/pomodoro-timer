package com.example.pomodoro_timer.data.data_access_objects;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.pomodoro_timer.model.CategoryModel;

@Dao
public interface CategoryDao {

    @Insert
    long insert(CategoryModel category);

    @Update
    int update(CategoryModel category);

    @Delete
    int delete(CategoryModel category);

    //@Query("SELECT * FROM CategoryModel")
    //public CategoryModel[] loadAllUsers();
}
