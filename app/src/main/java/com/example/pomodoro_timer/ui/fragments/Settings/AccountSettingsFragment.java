package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsAccountBinding;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;

public class AccountSettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsAccountBinding binding;
    private SettingsViewModel settingsVM;
    private SharedViewModel sharedVM;
    private boolean isLogin;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsAccountBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        init();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_account_container_view_id, new AccountSettingsControl())
                .commit();


        return binding.getRoot();
    }

    private void init(){
        listenUserLogin();
    }

    private void listenUserLogin(){
        sharedVM.getIsUserLoggedIn().observe(getViewLifecycleOwner(), isUserLoggedIn -> {
            if(isUserLoggedIn) {
                isLogin = true;
            }
        });
    }

}
