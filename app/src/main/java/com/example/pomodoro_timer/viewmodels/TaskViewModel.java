package com.example.pomodoro_timer.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro_timer.model.CategoryModel;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.utils.adapter.CategoryAdapter;
import com.example.pomodoro_timer.utils.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {

    //Fields
    private List<TaskModel> testTasks;
    private List<CategoryModel> testCategories;
    private CategoryAdapter categoryAdapter = new CategoryAdapter();
    private TaskAdapter adapter = new TaskAdapter();
    private MutableLiveData<List<TaskModel>> taskList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<CategoryModel>> categoryList = new MutableLiveData<>(new ArrayList<>());

    //Getters and Setters
    public LiveData<List<TaskModel>> getTaskList(){
        return taskList;
    }
    public TaskAdapter getAdapter(){
        return adapter;
    }
    public LiveData<List<CategoryModel>> getCategoryList(){
        return categoryList;
    }
    public CategoryAdapter getCategoryAdapter(){
        return categoryAdapter;
    }

    //THIS IS FOR TESTING PURPOSES ONLY
    //LATER USE FOR ACTUAL DATA FROM DATABASE FIREBASE
    public void initializeTasks(){
        testTasks = new ArrayList<>();
        testTasks.add(new TaskModel("Task 1", 4, 1));
        testTasks.add(new TaskModel("Task 2", 4, 2));
        testTasks.add(new TaskModel("Task 3", 4, 3));

        taskList.setValue(testTasks);
        adapter.setTasks(testTasks);

        Log.d("TaskViewModel", "TEST INITIALIZE TASK!");
    }
    public void initializeCategories(){
        testCategories = new ArrayList<>();
        testCategories.add(new CategoryModel("Category 1", "Category Icon 1"));
        testCategories.add(new CategoryModel("Category 2", "Category Icon 2"));
        testCategories.add(new CategoryModel("Category 3", "Category Icon 3"));

        categoryList.setValue(testCategories);
        categoryAdapter.setCategoryList(testCategories);
    }

    public void addTask(String taskTitle, int sessionCount, int priorityLevel){
        testTasks.add(new TaskModel(taskTitle, sessionCount, priorityLevel));
        taskList.setValue(testTasks);
        adapter.setTasks(testTasks);
    }

    public void addCategory(String categoryTitle, String icon){
        testCategories.add(new CategoryModel(categoryTitle, icon));
        categoryList.setValue(testCategories);
        categoryAdapter.setCategoryList(testCategories);
    }

}
