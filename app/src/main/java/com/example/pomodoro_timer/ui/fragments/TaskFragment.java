package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskBinding;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.ui.fragments.Tasks.EditCategoryFragment;
import com.example.pomodoro_timer.ui.fragments.Tasks.EditTaskFragment;
import com.example.pomodoro_timer.utils.adapter.CategoryAdapter;
import com.example.pomodoro_timer.utils.adapter.TaskAdapter;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskFragment extends Fragment {

    //Fields
    private FragmentTaskBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;
    private TaskAdapter taskAdapter;
    private CategoryAdapter categoryAdapter;
    private NavController navController;

    //Constructor
    public TaskFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = NavHostFragment.findNavController(this);

        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();

        sharedVM.setInAddMode(false);
        return binding.getRoot();
    }//End of onCreateView

    @Override
    public void onResume() {
        super.onResume();

        sharedVM.getInAddMode().observe(getViewLifecycleOwner(), inAddMode -> {
            if (inAddMode) {
                sharedVM.setInAddMode(false);

                NavController navController = NavHostFragment.findNavController(this);
                navController.popBackStack(R.id.menu_task, false);
            }
        });
    }//End of onResume

    private void initStuff(){
        taskAdapterHandle();
        categoryAdapterHandle();
        taskVM.initializeTasks();
        taskAdapter.setTasks(taskVM.getTaskList().getValue());
        taskVM.initializeCategories();
        categoryAdapter.setCategoryList(taskVM.getCategoryList().getValue());

        //For task recycler view
        binding.taskRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.taskRecyclerViewId.setAdapter(taskAdapter);
        //For category recycler view
        binding.categoryRecyclerViewId.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRecyclerViewId.setAdapter(categoryAdapter);

        featDraggableTask();
        onAddBtnClick();
        observeTaskAndCategory();
    }//End of initStuff method

    //Handle adapters
    private void taskAdapterHandle(){
        taskAdapter = new TaskAdapter(new TaskAdapter.TaskItemMenuListener(){

            @Override
            public void onEditTask(TaskModel task) {
                Log.d("TaskAdapter", "EDIT TASK CLICKED!");
                navController.navigate(R.id.editTaskFragment);
            }

            @Override
            public void onDeleteTask(TaskModel task) {
                if (taskVM.getTaskList().getValue() != null) {
                    taskVM.getTestTasks().remove(task);
                    taskVM.setTaskList(taskVM.getTestTasks()); // Notify observers
                    Log.d("TaskAdapter", "DELETE TASK CLICKED!");
                }
            }
        });
    }//End of taskAdapterHandle method

    private void categoryAdapterHandle(){
        categoryAdapter = new CategoryAdapter(() -> {
            navController.navigate(R.id.editCategoryFragment);
        });
    }//End of categoryAdapterHandle method

    private void observeTaskAndCategory(){
        taskVM.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);
        });
        taskVM.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategoryList(categories);
        });
    }

    private void onAddBtnClick(){
        sharedVM.getAddBtnClicked().observe(getViewLifecycleOwner(), clicked -> {
            if(clicked){
                NavHostFragment.findNavController(TaskFragment.this)
                        .navigate(R.id.action_menu_task_to_addFragment);
                sharedVM.getAddBtnClicked().setValue(false);
            }
        });
    }

    private void featDraggableTask(){
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(taskAdapter.getTaskList(), fromPosition, toPosition);
                taskAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(binding.taskRecyclerViewId);
    }//End of featDraggableTask method

}
