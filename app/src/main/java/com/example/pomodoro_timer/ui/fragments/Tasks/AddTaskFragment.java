package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskAddTaskBinding;
import com.example.pomodoro_timer.utils.dialogs.CategoryPickerDialog;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class AddTaskFragment extends Fragment {

    //Fields
    private FragmentTaskAddTaskBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;
    private NavController navController;
    private Integer userId;
    private boolean isUserLoggedIn = false;

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
        initializeThings();

        return binding.getRoot();
    }//End of onCreateView method

    private void initializeThings(){
        checkUser();
        onCancelBtnClick();
        onSaveBtnClick();
        onCategoryPickBtn();
        onRemoveCategoryBtn();
        observeCategory();
    }//End of initializeThings method

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();
        isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
    }//End of checkUser method

    private void clearCredentials(){
        //binding.priorityGroup.clearCheck();
        taskVM.clearTaskFields();
        taskVM.clearCategoryFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding.priorityGroup.clearCheck();
        taskVM.clearTaskFields();
        taskVM.clearCategoryFields();
    }

    //Button functions for cancel and save buttons

    private void onCancelBtnClick(){
        binding.cancelBtnId.setOnClickListener(v -> {
            clearCredentials();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);
        });
    }

    private void onSaveBtnClick(){
        binding.saveBtnId.setOnClickListener(v -> {
            String newTitle = taskVM.getTaskTitle().getValue();
            int newSessionCount = 0;
            int newPriority = 1;
            String taskDescription = taskVM.getTaskDescription().getValue();
            int newCategory = 0;

            try{
                newCategory = taskVM.getCategory().getValue().getId();
            }catch (NullPointerException e){
                Log.d("AddTaskFragment", "NULL POINTER EXCEPTION ENCOUNTERED: category title");
            }

            Log.d("CHECK CATEGORY TITLE","CATEGORY TITLE: " + newCategory);
            //Log.d("CHECK TASK DESCRIPTION", "TASK DESCRIPTION: " + taskDescription);

            //Exception handlers
            try {
                newSessionCount = Integer.parseInt(taskVM.getSessionCount().getValue());
            } catch (NumberFormatException e) {
                Log.d("AddTaskFragment", "EXCEPTION ENCOUNTERED: newSessionCount");
            }
            try {
                newPriority = taskVM.getPriorityLevel().getValue();
                Log.d("LOG_CHECK_PRIORITY", "newPriority: " + newPriority);
            } catch (NumberFormatException e) {
                Log.d("AddTaskFragment", "NUMBER FORMAT EXCEPTION ENCOUNTERED: newPriority");
            } catch (NullPointerException e){
                Log.d("AddTaskFragment", "NULL POINTER EXCEPTION ENCOUNTERED: newPriority");
            }

            if (isUserLoggedIn){
                taskVM.addTask(userId, newTitle, newSessionCount, newPriority, newCategory, taskDescription);
            }else{
                taskVM.addTask(1, newTitle, newSessionCount, newPriority, newCategory, taskDescription);
            }
            clearCredentials();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);
        });
    }//End of onSaveBtnClick methods

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
    }//End of onCategoryPickBtn method

    private void onRemoveCategoryBtn(){
        binding.removePickedCategoryBtnId.setOnClickListener(v -> {
            binding.addTaskCategoryBtnId.setVisibility(View.VISIBLE);
            binding.removePickedCategoryBtnId.setVisibility(View.GONE);
            binding.taskCategoryIconId.setVisibility(View.GONE);
            binding.taskCategoryTitleId.setVisibility(View.GONE);
            taskVM.clearCategoryFields();
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
