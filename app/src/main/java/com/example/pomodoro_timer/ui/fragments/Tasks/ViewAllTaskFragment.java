package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskViewAllTaskBinding;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.utils.adapter.FinishedTaskAdapter;
import com.example.pomodoro_timer.utils.adapter.TaskAdapter;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class ViewAllTaskFragment extends Fragment {

    //Fields
    private FragmentTaskViewAllTaskBinding binding;
    private TaskViewModel taskVM;
    private TaskAdapter taskAdapter;
    private FinishedTaskAdapter finishedTaskAdapter;
    private SharedViewModel sharedVM;
    private NavController navController;
    private Integer userId;
    private boolean isUserLoggedIn = false;

    public ViewAllTaskFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTaskViewAllTaskBinding.inflate(inflater, container, false);
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
        displayTask();
        taskAdapterHandler();

        binding.ongoingTasksRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ongoingTasksRecyclerViewId.setAdapter(taskAdapter);
        binding.finishedTasksRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.finishedTasksRecyclerViewId.setAdapter(finishedTaskAdapter);

        clearCompletedTasks();
        observeTasks();
    }//End of initializeStuff method

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();
        isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
    }//End of checkUser method

    private void displayTask(){
        if (isUserLoggedIn){
            taskVM.displayOngoingTask(userId);
            taskVM.displayCompletedTask(userId);
        }else {
            taskVM.displayOngoingTask(1);
            taskVM.displayCompletedTask(1);
        }
    }//End of displayTask method

    private void taskAdapterHandler(){
        taskAdapter = new TaskAdapter(new TaskAdapter.TaskItemMenuListener() {

            @Override
            public void onEditTask(TaskModel task) {
                taskVM.loadEditTask(task);
                navController.navigate(R.id.editTaskFragment);
            }

            @Override
            public void onDeleteTask(TaskModel task) {
                if (taskVM.getInProgressTaskList().getValue() != null) {
                    taskVM.deleteInProgressTask(task);
                }
            }
        });
        finishedTaskAdapter = new FinishedTaskAdapter(task -> {
            taskVM.reAddTask(task);
            displayTask();
        });
    }//End of taskAdapterHandler method

    private void clearCompletedTasks(){
        binding.clearFinishedTasksButtonId.setOnClickListener(v -> {
            taskVM.clearAllFinishedTasks(userId);
            displayTask();
        });
    }//End of clearCompletedTasks method

    private void observeTasks(){
        taskVM.getInProgressTaskList().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);
        });
        taskVM.getFinishedTaskList().observe(getViewLifecycleOwner(), tasks -> {
            finishedTaskAdapter.setTasks(tasks);
        });
    }//End of observeTasks method

}
