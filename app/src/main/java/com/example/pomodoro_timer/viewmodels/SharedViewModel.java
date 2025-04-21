package com.example.pomodoro_timer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    //Fields
    private final MutableLiveData<Boolean> showAddTaskBtn = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> addBtnClicked = new MutableLiveData<>();

    private final MutableLiveData<Boolean> inAddMode = new MutableLiveData<>(false);

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

}
