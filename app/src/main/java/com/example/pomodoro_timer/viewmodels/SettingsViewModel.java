package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.UserModel;
import com.example.pomodoro_timer.utils.SingleLiveEvent;
import com.example.pomodoro_timer.utils.shared_preferences.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase db;
    private final ExecutorService executor;
    private final SessionManager sessionManager;
    private final SingleLiveEvent<Boolean> loginResult = new SingleLiveEvent<>();
    private final MutableLiveData<String> toastLoginResultMessage = new MutableLiveData<>();

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
    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }
    public LiveData<String> getToastLoginResultMessage() {
        return toastLoginResultMessage;
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
    public MutableLiveData<String> getSelectedTheme() {
        return selectedTheme;
    }
    public MutableLiveData<String> getSelectedAlarm() {
        return selectedAlarm;
        }
    public MutableLiveData<Boolean> getAllowNotifications() {
        return allowNotifications;
    }
    public MutableLiveData<Boolean> getAllowNudge() {
        return allowNudge;
    }
    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme.setValue(selectedTheme);
    }
    public void setSelectedAlarm(String selectedAlarm) {
        this.selectedAlarm.setValue(selectedAlarm);
    }
    public void setAllowNotifications(boolean allowNotifications) {
        this.allowNotifications.setValue(allowNotifications);
    }
    public void setAllowNudge(boolean allowNudge) {
        this.allowNudge.setValue(allowNudge);
    }

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
    public SettingsViewModel(@NonNull Application application){
        super(application);
        sessionManager = new SessionManager(application);
        db = AppDatabase.getInstance(application);
        executor = Executors.newSingleThreadExecutor();
    }//End of constructor

    //LOGIN VIEWMODEL FUNCTIONS
    public void login(){
        executor.execute(() -> {
            UserModel user = db.userDao().getUserByUsernameNow(loginUsername.getValue());

            if (user == null){
                Log.d("SettingsViewModel", "User not found");
                toastLoginResultMessage.postValue("User not found");
                loginResult.postValue(false);
                return;
            }
            if (user.getPassword().equals(loginPassword.getValue())){
                sessionManager.saveLoginSession(user.getId(), user.getUsername());
                loginResult.postValue(true);
                Log.d("SettingsViewModel", "Login Successful");
                Log.d("USER INFO", "Username: " + user.getUsername() + " Password: " + user.getPassword() + " Email: " + user.getEmail() + " ID: " + user.getId());
            } else {
                Log.d("SettingsViewModel", "Login Failed");
                toastLoginResultMessage.postValue("Login Failed: Wrong password or username");
                loginResult.postValue(false);
            }
        });
    }//End of login method

    //SIGN UP VIEWMODEL FUNCTIONS
    public void signUp(){
        executor.execute(() -> {
            if(signUpPassword.getValue().equals(signUpConfirmPassword.getValue())){
                UserModel registerUser = new UserModel(signUpUsername.getValue(), signUpPassword.getValue(), "");
                if (db.userDao().getUserByUsernameNow(registerUser.getUsername()) != null){
                    toastLoginResultMessage.postValue("Username already exists");
                    loginResult.postValue(false);
                    return;
                }
                db.userDao().insert(registerUser);
                sessionManager.saveLoginSession(registerUser.getId(), registerUser.getUsername()); //Remind me to modify sessionManager instance in SettingsViewModel or other ViewModel, Reason: Tightly coupled
                loginUsername.postValue(signUpUsername.getValue());
                loginResult.postValue(true);
                clearSignUpFields();
                Log.d("NEW USER INFO", "Username: " + signUpUsername.getValue() + " Password: " + signUpPassword.getValue());
            }
            else{
                toastLoginResultMessage.postValue("Passwords do not match");
                loginResult.postValue(false);
            }
        });
    }//End of signUp method

    private void clearSignUpFields(){
        signUpUsername.postValue("");
        signUpPassword.postValue("");
        signUpConfirmPassword.postValue("");
    }//End of clearSignUpFields method

    //For testing purposes
    public void createTestUser(){
        UserModel testUser = new UserModel("testuser", "1234", "test@example.com");
        executor.execute(() -> {
            if (db.userDao().getUserByUsername("testuser") == null) {
                db.userDao().insert(testUser);
            }
        });
    }

}
