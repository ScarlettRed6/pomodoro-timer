package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;

public class AccountSettingsFragment extends Fragment {

    //Fields
    private SettingsViewModel settingsVM;
    private SharedViewModel sharedVM;
    private boolean isLogin;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        return inflater.inflate(R.layout.fragment_settings_account, container, false);
    }

    private void init(){
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
    }

    private void listenUserLogin(){
        sharedVM.getIsUserLoggedIn().observe(getViewLifecycleOwner(), isUserLoggedIn -> {
            if(isUserLoggedIn) {
                isLogin = true;
            }
        });
    }

}
