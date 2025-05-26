package com.example.pomodoro_timer.data.network.dto;

public class User {

    //Fields
    private int id;
    private String email;
    private String password;
    private boolean isLoggedIn;

    public User() {} //This is Required for Retrofit

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.isLoggedIn = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean getIsLoggedIn() { return isLoggedIn; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setIsLoggedIn(boolean loggedIn) { isLoggedIn = loggedIn; }

}
