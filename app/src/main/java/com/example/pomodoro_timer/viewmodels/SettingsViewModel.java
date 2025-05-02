package com.example.pomodoro_timer.viewmodels;

import android.util.Log;

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
