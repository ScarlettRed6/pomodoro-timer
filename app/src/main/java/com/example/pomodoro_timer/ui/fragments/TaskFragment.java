package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskBinding;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class TaskFragment extends Fragment {

    //Fields
    private FragmentTaskBinding binding;
    private TaskViewModel taskVM;
    public TaskFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(this);

        //Initialize tasks and categories, it populates the lists
        taskVM.initializeTasks();
        taskVM.initializeCategories();

        //For task recycler view
        binding.taskRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.taskRecyclerViewId.setAdapter(taskVM.getAdapter());
        //For category recycler view
        binding.categoryRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRecyclerViewId.setAdapter(taskVM.getCategoryAdapter());

        return binding.getRoot();
    }

}
