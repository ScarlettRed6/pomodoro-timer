package com.example.pomodoro_timer.data.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro_timer.model.UserModel;

@Dao
public interface UserDao {

    @Insert
    long insert(UserModel user);

    @Update
    int update(UserModel user);

    @Delete
    int delete(UserModel user);

    @Query("SELECT * FROM users")
    UserModel getAllUsers();

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserModel getUserByEmailNow(String email); //for login

    @Query("SELECT * FROM users WHERE id = :id")
    UserModel getUserById(int id);

    @Query("SELECT EXISTS(SELECT 1 FROM tasks WHERE user_id = :userId)")
    boolean hasTask(int userId);

    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE user_id = :userId)")
    boolean hasCategory(int userId);

    //@Query("SELECT * FROM users")
    //public UserModel[] loadAllUsers();

}
