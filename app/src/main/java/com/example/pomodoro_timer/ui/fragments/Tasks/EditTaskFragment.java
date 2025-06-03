package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentEditTaskBinding;
import com.example.pomodoro_timer.utils.dialogs.CategoryPickerDialog;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class EditTaskFragment extends Fragment {

    //Fields
    private FragmentEditTaskBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;
    private NavController navController;
    private Integer userId;
    private boolean isUserLoggedIn = false;

    public EditTaskFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = NavHostFragment.findNavController(EditTaskFragment.this);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initializeStuff();

        return binding.getRoot();
    }//End of onCreateView

    private void initializeStuff(){
        checkUser();
        observeCategory();
        onSaveBtnClick();
        onCancelBtnClick();
        onCategoryPickBtn();
        onRemoveCategoryBtn();
    }//End of initializeStuff method

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.priorityGroup.clearCheck();
        taskVM.clearTaskFields();
        taskVM.clearCategoryFields();
    }

    private void clearCredentials(){
        binding.priorityGroup.clearCheck();
        taskVM.clearTaskFields();
        taskVM.clearCategoryFields();
    }

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();
        isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
    }//End of checkUser method

    private void onCancelBtnClick(){
        binding.cancelBtnId.setOnClickListener(v -> {
            clearCredentials();
            navController.popBackStack(R.id.menu_task, false);
        });
    }

    private void onSaveBtnClick(){
        binding.saveBtnId.setOnClickListener(v -> {
            String taskTitle = taskVM.getTaskTitle().getValue();
            int sessionCount = Integer.parseInt(taskVM.getSessionCount().getValue());
            int priorityLevel = taskVM.getPriorityLevel().getValue();
            String taskDescription = taskVM.getTaskDescription().getValue();
            int taskId = taskVM.getTaskId().getValue();
            int categoryId = taskVM.getCategory().getValue() != null ? taskVM.getCategory().getValue().getId() : 0;

            if (taskTitle != null && !taskTitle.isEmpty() && sessionCount > 0 && priorityLevel > 0 && taskDescription != null) {
                if (isUserLoggedIn) {
                    taskVM.updateTask(userId, taskId, taskTitle, sessionCount, priorityLevel, categoryId, taskDescription);
                } else {
                    taskVM.updateTask(1, taskId, taskTitle, sessionCount, priorityLevel, categoryId, taskDescription);
                }
            }//End of if statement
            navController.popBackStack(R.id.menu_task, false);
            clearCredentials();
        });
    }//End of onSaveBtnClick method

    private void onCategoryPickBtn(){
        binding.addTaskCategoryBtnId.setOnClickListener(v -> {
            CategoryPickerDialog dialog = new CategoryPickerDialog();
            int catListLength = taskVM.getCategoryList().getValue().size();
            if (catListLength > 0){
                dialog.show(getParentFragmentManager(), "CategoryPickerDialog");
            }else {
                Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onRemoveCategoryBtn(){
        binding.removePickedCategoryBtnId.setOnClickListener(v -> {
            binding.addTaskCategoryBtnId.setVisibility(View.VISIBLE);
            binding.removePickedCategoryBtnId.setVisibility(View.GONE);
            binding.taskCategoryIconId.setVisibility(View.GONE);
            binding.taskCategoryTitleId.setVisibility(View.GONE);
            taskVM.setCategory(null);
        });
    }//End of onRemoveCategoryBtn method

    private void observeCategory(){
        taskVM.getCategory().observe(getViewLifecycleOwner(), category -> {
            if (category != null) {
                binding.addTaskCategoryBtnId.setVisibility(View.GONE);
                binding.taskCategoryIconId.setVisibility(View.VISIBLE);
                binding.taskCategoryTitleId.setVisibility(View.VISIBLE);
                binding.removePickedCategoryBtnId.setVisibility(View.VISIBLE);

                // --- FIX IS HERE ---
                if (category.getIcon() != null && category.getIcon() != 0) {
                    android.util.TypedValue typedValue = new android.util.TypedValue();
                    // Use the context of the ImageView itself or the fragment's view context
                    android.content.Context context = binding.taskCategoryIconId.getContext();
                    context.getTheme().resolveAttribute(category.getIcon(), typedValue, true);

                    if (typedValue.resourceId != 0) {
                        binding.taskCategoryIconId.setImageResource(typedValue.resourceId);
                    } else {
                        // Fallback or error: Attribute couldn't be resolved
                        binding.taskCategoryIconId.setVisibility(View.GONE); // Hide icon if not found
                        Log.e(getTag(), "Could not resolve attribute to drawable: " + Integer.toHexString(category.getIcon()));
                    }
                } else {
                    // No icon ID in category model
                    binding.taskCategoryIconId.setVisibility(View.GONE);
                }
                // --- END OF FIX ---

                binding.taskCategoryTitleId.setText(category.getCategoryTitle());
            } else {
                binding.addTaskCategoryBtnId.setVisibility(View.VISIBLE);
                binding.removePickedCategoryBtnId.setVisibility(View.GONE);
                binding.taskCategoryIconId.setVisibility(View.GONE);
                binding.taskCategoryTitleId.setVisibility(View.GONE);
            }
        });
    }//End of observeCategory method

}




