package com.example.pomodoro_timer.utils.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    //Fields
    private static final String PREF_NAME = "pomodoro_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences sharedPreferences;

    //Constructor
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginSession(int userId, String username){
        sharedPreferences.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    public void clearLoginSession(){
        sharedPreferences.edit().clear().apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId(){
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME, "Username");
    }

}
