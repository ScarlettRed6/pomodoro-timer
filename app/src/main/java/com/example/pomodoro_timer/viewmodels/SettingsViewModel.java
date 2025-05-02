package com.example.pomodoro_timer.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    //Fields
    private SharedViewModel sharedVM;
    private final MutableLiveData<Boolean> isLogin = new MutableLiveData<>();

    //Getters and Setters
    public MutableLiveData<Boolean> getIsLogin() {
        return isLogin;
    }
    private void setIsLogin(boolean isLogin) {
        this.isLogin.setValue(isLogin);
    }

    public SettingsViewModel(){
        sharedVM = new SharedViewModel();
        try{
            setIsLogin(sharedVM.getIsUserLoggedIn().getValue());
        }catch (NullPointerException e){
            Log.d("SettingsViewModel", "AN EXCEPTION HAPPENED! SETTINGS VIEW MODEL!");
        }
    }

}
