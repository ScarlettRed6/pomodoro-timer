package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsAccountBindingImpl;
import com.example.pomodoro_timer.databinding.FragmentSettingsBinding;
import com.example.pomodoro_timer.ui.fragments.Settings.AccountSettingsFragment;
import com.example.pomodoro_timer.utils.adapter.SettingsPagerAdapter;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class SettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsVM;
    private boolean isLogin;

    public SettingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
//        binding.settingsTabLayoutId.addTab(binding.settingsTabLayoutId.newTab().setText("Account"));
//        binding.settingsTabLayoutId.addTab(binding.settingsTabLayoutId.newTab().setText("Appearance"));
//        binding.settingsTabLayoutId.addTab(binding.settingsTabLayoutId.newTab().setText("Notifications"));

        initStuff();

        return binding.getRoot();
    }//End of onCreateView method

    private void initStuff(){
        isLogin = settingsVM.getIsLogin().getValue();//Sets isLogin to true or false
        pagerAdapter();
    }

    private void pagerAdapter(){
        SettingsPagerAdapter adapter = new SettingsPagerAdapter(this, isLogin);
        Log.d("ACCOUNT STATUS", "IS LOGIN? " + isLogin);
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
    }//End of pagerAdapter method

}
