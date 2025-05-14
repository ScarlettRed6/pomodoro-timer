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

    @Query("SELECT * FROM users WHERE username = :username")
    LiveData<UserModel> getUserByUsername(String username); //for observing

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserModel getUserByUsernameNow(String username); //for login

    //@Query("SELECT * FROM users")
    //public UserModel[] loadAllUsers();

}
