package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "users")
public class UserModel {

    //Fields
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "is_logged_in")
    private boolean isLoggedIn;

    public UserModel(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLoggedIn = false;
    }

    //Getters and setters
    public int getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }


}
