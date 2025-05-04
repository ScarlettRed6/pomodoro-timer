package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsAccountControlsBinding;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;

public class AccountSettingsControl extends Fragment {

    //Fields
    private FragmentSettingsAccountControlsBinding binding;
    private SettingsViewModel settingsVM;

    public AccountSettingsControl(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSettingsAccountControlsBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        binding.editProfileLayoutId.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_account_container_view_id, new AccountSettingsEditProfile())
                    .commit();

        });

        return binding.getRoot();
    }

}
