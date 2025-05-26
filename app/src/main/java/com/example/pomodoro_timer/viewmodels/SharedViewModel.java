package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharedViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase db;
    private final ExecutorService executor;
    private final MutableLiveData<Boolean> showAddTaskBtn = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> addBtnClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> inAddMode = new MutableLiveData<>(false);

    //User logged in fields
    private final MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>(false); //Set true or false for testing purposes
    private final MutableLiveData<String> currentEmail = new MutableLiveData<>("Username");
    private final MutableLiveData<Integer> currentUserId = new MutableLiveData<>(0);

    //Getters and Setters
    public MutableLiveData<Boolean> getShowAddTaskBtn(){
        return showAddTaskBtn;
    }
    public void setShowAddTaskBtn(boolean show){
        showAddTaskBtn.setValue(show);
    }
    public void onAddBtnClick(){
        addBtnClicked.setValue(true);
    }
    public MutableLiveData<Boolean> getAddBtnClicked(){
        return addBtnClicked;
    }
    public void setInAddMode(boolean value) {
        inAddMode.setValue(value);
    }
    public LiveData<Boolean> getInAddMode() {
        return inAddMode;
    }

    //User getters and Setters
    public MutableLiveData<Boolean> getIsUserLoggedIn() {
        return isUserLoggedIn;
    }
    public void setIsUserLoggedIn(boolean value) {
        this.isUserLoggedIn.postValue(value);
    }
    public LiveData<String> getCurrentEmail() {
        return currentEmail;
    }
    public void setCurrentEmail(String value) {
        currentEmail.setValue(value);
    }
    public LiveData<Integer> getCurrentUserId() {
        return currentUserId;
    }
    public void setCurrentUserId(int value) {
        currentUserId.setValue(value);
    }

    //Constructor
    public SharedViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        executor = Executors.newSingleThreadExecutor();
    }

    public void ensureGuestUserExists() {
        executor.execute(() -> {
            if (db.userDao().getUserById(1) == null) {
                UserModel guestUser = new UserModel("guest@guest", "guest");
                guestUser.setId(1);
                db.userDao().insert(guestUser);
            }
        });
    }

}
