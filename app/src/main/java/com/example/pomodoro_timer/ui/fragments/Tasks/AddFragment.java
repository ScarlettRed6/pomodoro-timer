package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.utils.adapter.AddPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AddFragment extends Fragment {

    //Fields
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    public AddFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_task_add, container, false);

        viewPager2 = view.findViewById(R.id.task_pager2_id);
        tabLayout = view.findViewById(R.id.task_tab_layout_id);

        AddPagerAdapter adapter = new AddPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("New Task");
            } else {
                tab.setText("New Category");
            }
        }).attach();
        return view;
    }

}
