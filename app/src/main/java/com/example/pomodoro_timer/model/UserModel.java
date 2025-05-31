package com.example.pomodoro_timer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "users")
public class UserModel {

    //Fields
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "is_logged_in")
    private boolean isLoggedIn;

    @ColumnInfo(name = "firebase_uid")
    private String firebaseUid;

    public UserModel() {
        // Firestore requires a no‐arg constructor
    }
    public UserModel(String email, String password){
        this.password = password;
        this.email = email;
        this.isLoggedIn = false;
    }

    //Getters and setters
    public int getId(){
        return id;
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
    public String getFirebaseUid() { return firebaseUid; }
    public void setId(int id) {
        this.id = id;
    }
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }


}
