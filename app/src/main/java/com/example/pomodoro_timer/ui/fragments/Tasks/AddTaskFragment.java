package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskAddTaskBinding;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class AddTaskFragment extends Fragment {

    //Fields
    private FragmentTaskAddTaskBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;
    private NavController navController;

    public AddTaskFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTaskAddTaskBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        navController = NavHostFragment.findNavController(AddTaskFragment.this);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(this);

        //Set the add mode to true
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedVM.setInAddMode(true);

        //Add other contexts here
        onCancelBtnClick();
        onSaveBtnClick();

        return binding.getRoot();
    }//End of onCreateView method

    //Button functions for cancel and save buttons

    private void onCancelBtnClick(){
        binding.cancelBtnId.setOnClickListener(v -> {
            taskVM.clearTaskFields();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);
        });
    }

    private void onSaveBtnClick(){
        binding.saveBtnId.setOnClickListener(v -> {
            String newTitle = taskVM.getTaskTitle().getValue();
            int newSessionCount = 0;
            int newPriority = 1;

            //Exception handlers
            try {
                newSessionCount = Integer.parseInt(taskVM.getSessionCount().getValue());
            } catch (NumberFormatException e) {
                Log.d("AddTaskFragment", "EXCEPTION ENCOUNTERED: newSessionCount");
            }
            try {
                newPriority = taskVM.getPriorityLevel().getValue();
            } catch (NumberFormatException e) {
                Log.d("AddTaskFragment", "NUMBER FORMAT EXCEPTION ENCOUNTERED: newPriority");
            } catch (NullPointerException e){
                Log.d("AddTaskFragment", "NULL POINTER EXCEPTION ENCOUNTERED: newPriority");
            }

            taskVM.addTask(newTitle, newSessionCount, newPriority);
            taskVM.clearTaskFields();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);
        });
    }//End of onSaveBtnClick methods

}
