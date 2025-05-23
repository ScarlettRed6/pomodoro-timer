package com.example.pomodoro_timer.data.data_access_objects;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.CategoryModel;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    long insert(CategoryModel category);

    @Update
    int update(CategoryModel category);

    @Delete
    int delete(CategoryModel category);

    @Query("SELECT * FROM categories WHERE user_Id = :userId")
    List<CategoryModel> getAllCategories(int userId);

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    CategoryModel getCategoryById(int categoryId);

    //@Query("SELECT * FROM CategoryModel")
    //public CategoryModel[] loadAllUsers();
}
