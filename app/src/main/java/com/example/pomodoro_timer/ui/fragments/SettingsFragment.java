package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsBinding;
import com.example.pomodoro_timer.utils.adapter.SettingsPagerAdapter;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class SettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsVM;

    public SettingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(this);

        //Context here
//        binding.settingsTabLayoutId.addTab(binding.settingsTabLayoutId.newTab().setText("Account"));
//        binding.settingsTabLayoutId.addTab(binding.settingsTabLayoutId.newTab().setText("Appearance"));
//        binding.settingsTabLayoutId.addTab(binding.settingsTabLayoutId.newTab().setText("Notifications"));

        SettingsPagerAdapter adapter = new SettingsPagerAdapter(this);
        binding.settingsViewpager2Id.setAdapter(adapter);

        new TabLayoutMediator(binding.settingsTabLayoutId, binding.settingsViewpager2Id, (tab, position) -> {
            if (position == 0) {
                tab.setText("Account");
            } else if (position == 1) {
                tab.setText("Timer");
            } else {
                tab.setText("Notifications");
            }
        }).attach();

        return binding.getRoot();
    }

}
