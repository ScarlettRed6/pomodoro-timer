package com.example.pomodoro_timer.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pomodoro_timer.ui.fragments.Tasks.AddCategoryFragment;
import com.example.pomodoro_timer.ui.fragments.Tasks.AddTaskFragment;

public class AddPagerAdapter extends FragmentStateAdapter {

    public AddPagerAdapter(@NonNull Fragment fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AddTaskFragment();
            case 1:
                return new AddCategoryFragment();
            default:
                return new AddTaskFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
