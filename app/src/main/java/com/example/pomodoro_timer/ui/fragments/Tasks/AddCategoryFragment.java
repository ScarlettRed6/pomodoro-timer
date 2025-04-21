package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;

public class AddCategoryFragment extends Fragment {

    //Fields
    private SharedViewModel sharedVM;

    public AddCategoryFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedVM.setInAddMode(true);
        return inflater.inflate(R.layout.fragment_task_add_category, container, false);
    }

}
