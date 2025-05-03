package com.example.pomodoro_timer.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    //Fields
    private SharedViewModel sharedVM;
    private final MutableLiveData<Boolean> isLogin = new MutableLiveData<>();

    //Login Fields
    private final MutableLiveData<String> loginUsername = new MutableLiveData<>();
    private final MutableLiveData<String> loginPassword = new MutableLiveData<>();

    //Sign up Fields
    private final MutableLiveData<String> signUpUsername = new MutableLiveData<>();
    private final MutableLiveData<String> signUpPassword = new MutableLiveData<>();
    private final MutableLiveData<String> signUpConfirmPassword = new MutableLiveData<>();

    //Themes Fields
    private final MutableLiveData<String> selectedTheme = new MutableLiveData<>("Theme 1");
    private final MutableLiveData<String> selectedAlarm = new MutableLiveData<>("Alarm 1");
    private final MutableLiveData<Boolean> allowNotifications = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> allowNudge = new MutableLiveData<>(true);

    //Timer Fields (Pomodoro, Short and Long break Timer values)
    private final MutableLiveData<Integer> pomodoroMinutes= new MutableLiveData<>(25);
    private final MutableLiveData<Integer> pomodoroSeconds= new MutableLiveData<>(0);
    private final MutableLiveData<Integer> shortBreakMinutes= new MutableLiveData<>(5);
    private final MutableLiveData<Integer> shortBreakSeconds= new MutableLiveData<>(0);
    private final MutableLiveData<Integer> longBreakMinutes= new MutableLiveData<>(15);
    private final MutableLiveData<Integer> longBreakSeconds= new MutableLiveData<>(0);


    //Getters and Setters
    public MutableLiveData<Boolean> getIsLogin() {
        return isLogin;
    }
    private void setIsLogin(boolean isLogin) {
        this.isLogin.setValue(isLogin);
    }

    //Login getters and setters
    public MutableLiveData<String> getLoginUsername() {
        return loginUsername;
    }
    public MutableLiveData<String> getLoginPassword() {
        return loginPassword;
    }
    public void setLoginUsername(String loginUsername) {
        this.loginUsername.setValue(loginUsername);
    }
    public void setLoginPassword(String loginPassword) {
        this.loginPassword.setValue(loginPassword);
    }

    //Sign up getters and setters
    public MutableLiveData<String> getSignUpUsername() {
        return signUpUsername;
    }
    public MutableLiveData<String> getSignUpPassword() {
        return signUpPassword;
    }
    public MutableLiveData<String> getSignUpConfirmPassword() {
        return signUpConfirmPassword;
    }
    public void setSignUpUsername(String signUpUsername) {
        this.signUpUsername.setValue(signUpUsername);
    }
    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword.setValue(signUpPassword);
    }
    public void setSignUpConfirmPassword(String signUpConfirmPassword) {
        this.signUpConfirmPassword.setValue(signUpConfirmPassword);
    }

    //Themes Getters and Setters


    //Timer Getters and Setters
    public LiveData<Integer> getPomodoroMinutes() {
        return pomodoroMinutes;
    }
    public LiveData<Integer> getPomodoroSeconds() {
        return pomodoroSeconds;
    }
    public LiveData<Integer> getShortBreakMinutes() {
        return shortBreakMinutes;
    }
    public LiveData<Integer> getShortBreakSeconds() {
        return shortBreakSeconds;
        }
    public LiveData<Integer> getLongBreakMinutes() {
        return longBreakMinutes;
    }
    public LiveData<Integer> getLongBreakSeconds() {
        return longBreakSeconds;
    }
    public void setPomodoroMinutes(int pomodoroMinutes) {
        this.pomodoroMinutes.setValue(pomodoroMinutes);
    }
    public void setPomodoroSeconds(int pomodoroSeconds) {
        this.pomodoroSeconds.setValue(pomodoroSeconds);
    }
    public void setShortBreakMinutes(int shortBreakMinutes) {
        this.shortBreakMinutes.setValue(shortBreakMinutes);
    }
    public void setShortBreakSeconds(int shortBreakSeconds) {
        this.shortBreakSeconds.setValue(shortBreakSeconds);
    }
    public void setLongBreakMinutes(int longBreakMinutes) {
        this.longBreakMinutes.setValue(longBreakMinutes);
    }
    public void setLongBreakSeconds(int longBreakSeconds) {
        this.longBreakSeconds.setValue(longBreakSeconds);
    }

    //Constructor
    public SettingsViewModel(){
        sharedVM = new SharedViewModel();
        try{
            setIsLogin(sharedVM.getIsUserLoggedIn().getValue());
        }catch (NullPointerException e){
            Log.d("SettingsViewModel", "AN EXCEPTION HAPPENED! SETTINGS VIEW MODEL!");
        }
    }

}
