package com.example.pomodoro_timer.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    //Fields
    private final MutableLiveData<Boolean> showAddTaskBtn = new MutableLiveData<>(false);

    //Getters and Setters
    public MutableLiveData<Boolean> getShowAddTaskBtn(){
        return showAddTaskBtn;
    }
    public void setShowAddTaskBtn(boolean show){
        showAddTaskBtn.setValue(show);
    }

}
