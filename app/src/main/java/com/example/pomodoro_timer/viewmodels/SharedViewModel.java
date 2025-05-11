package com.example.pomodoro_timer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    //Fields
    private final MutableLiveData<Boolean> showAddTaskBtn = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> addBtnClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> inAddMode = new MutableLiveData<>(false);

    //User logged in fields
    private final MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>(false); //Set true or false for testing purposes
    private final MutableLiveData<String> currentUsername = new MutableLiveData<>("Username");
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
    public LiveData<String> getCurrentUsername() {
        return currentUsername;
    }
    public void setCurrentUsername(String value) {
        currentUsername.setValue(value);
    }
    public LiveData<Integer> getCurrentUserId() {
        return currentUserId;
    }
    public void setCurrentUserId(int value) {
        currentUserId.setValue(value);
    }

}
