package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.databinding.FragmentEditTaskBinding;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class EditTaskFragment extends Fragment {

    //Fields
    private FragmentEditTaskBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;

    public EditTaskFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initializeStuff();

        return binding.getRoot();
    }//End of onCreateView

    private void initializeStuff(){
        observeCategory();
    }

    private void onSaveBtnClick(){
        binding.saveBtnId.setOnClickListener(v -> {
            String taskTitle = taskVM.getTaskTitle().getValue();
            int sessionCount = Integer.parseInt(taskVM.getSessionCount().getValue());
            int priorityLevel = taskVM.getPriorityLevel().getValue();
            String taskDescription = taskVM.getTaskDescription().getValue();
            int categoryId = taskVM.getCategory().getValue() != null ? taskVM.getCategory().getValue().getId() : 0;

        });
    }

    private void observeCategory(){
        taskVM.getCategory().observe(getViewLifecycleOwner(), category -> {
            if (category != null) {
                binding.addTaskCategoryBtnId.setVisibility(View.GONE);
                binding.taskCategoryIconId.setVisibility(View.VISIBLE);
                binding.taskCategoryTitleId.setVisibility(View.VISIBLE);
                binding.removePickedCategoryBtnId.setVisibility(View.VISIBLE);
                binding.taskCategoryIconId.setImageResource(category.getIcon());
                binding.taskCategoryTitleId.setText(category.getCategoryTitle());
            }else{
                binding.addTaskCategoryBtnId.setVisibility(View.VISIBLE);
                binding.removePickedCategoryBtnId.setVisibility(View.GONE);
                binding.taskCategoryIconId.setVisibility(View.GONE);
                binding.taskCategoryTitleId.setVisibility(View.GONE);
            }
        });
    }//End of observeCategory method

}




