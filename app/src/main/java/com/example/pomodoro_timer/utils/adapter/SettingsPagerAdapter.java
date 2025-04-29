package com.example.pomodoro_timer.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pomodoro_timer.ui.fragments.Settings.AccountSettingsFragment;
import com.example.pomodoro_timer.ui.fragments.Settings.ThemeSettingsFragment;
import com.example.pomodoro_timer.ui.fragments.Settings.TimerSettingsFragment;

public class SettingsPagerAdapter extends FragmentStateAdapter {

    public SettingsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AccountSettingsFragment();
            case 1:
                return new TimerSettingsFragment();
            case 2:
                return new ThemeSettingsFragment();
            default:
                return new AccountSettingsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
