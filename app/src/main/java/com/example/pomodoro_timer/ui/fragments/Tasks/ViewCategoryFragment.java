package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pomodoro_timer.databinding.FragmentTaskViewCategoryBinding;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.utils.adapter.TaskAdapter;
import com.example.pomodoro_timer.utils.adapter.ViewTaskAdapter;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class ViewCategoryFragment extends Fragment {

    //Fields
    private FragmentTaskViewCategoryBinding binding;
    private TaskViewModel taskVM;
    private SharedViewModel sharedVM;
    private ViewTaskAdapter taskAdapter;
    private NavController navController;
    private Integer userId;
    private boolean isUserLoggedIn = false;

    public ViewCategoryFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = FragmentTaskViewCategoryBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = NavHostFragment.findNavController(this);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initializeStuff();

        return binding.getRoot();
    }//End of onCreateView

    private void initializeStuff(){
        checkUser();
        displayCategoryTasks();
        taskAdapterHandle();

        binding.categoryTasksRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.categoryTasksRecyclerViewId.setAdapter(taskAdapter);

        observeTask();
    }//End of initializeStuff method

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();
        isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
    }//End of checkUser method

    private void displayCategoryTasks(){
        int categoryId = taskVM.getCategory().getValue().getId();
        if (isUserLoggedIn)
            taskVM.displayTaskByCategory(userId, categoryId);
        else
            taskVM.displayTaskByCategory(1, categoryId);
    }//End of displayCategoryTasks method

    private void taskAdapterHandle(){
        taskAdapter = new ViewTaskAdapter(task -> {
            taskVM.removeCategoryOfTask(userId, task.getCategoryId(), task.getId());
        });
    }//End of taskAdapterHandle method

    private void observeTask(){
        taskVM.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);
        });
    }

}
