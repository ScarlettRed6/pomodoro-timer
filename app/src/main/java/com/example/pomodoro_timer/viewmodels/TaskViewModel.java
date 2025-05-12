package com.example.pomodoro_timer.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.model.CategoryModel;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.utils.adapter.CategoryAdapter;
import com.example.pomodoro_timer.utils.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {

    //Fields
    private List<TaskModel> testTasks = new ArrayList<>();
    private List<CategoryModel> testCategories = new ArrayList<>();
    //private CategoryAdapter categoryAdapter = new CategoryAdapter();
    //private TaskAdapter adapter = new TaskAdapter();

    //Task list
    private MutableLiveData<List<TaskModel>> taskList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<TaskModel> firstTask = new MutableLiveData<>();
    private MutableLiveData<List<CategoryModel>> categoryList = new MutableLiveData<>(new ArrayList<>());

    //Task fields
    private final MutableLiveData<String> taskTitle = new MutableLiveData<>("");
    private final MutableLiveData<String> sessionCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> priorityLevel = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> selectedPriority = new MutableLiveData<>();
    private final MutableLiveData<String> taskDescription = new MutableLiveData<>("");

    //Category fields
    private MutableLiveData<String> categoryTitle = new MutableLiveData<>("");
    private MutableLiveData<Integer> categoryIcon = new MutableLiveData<>();
    private MutableLiveData<CategoryModel> category = new MutableLiveData<>();

    //Getters and Setters
    public List<TaskModel> getTestTasks(){
        return testTasks;
    }
    public LiveData<List<TaskModel>> getTaskList(){
        return taskList;
    }
    public LiveData<TaskModel> getFirstTask(){
        return firstTask;
    }
//    public TaskAdapter getAdapter(){
//        return adapter;
//    }
    public LiveData<List<CategoryModel>> getCategoryList(){
        return categoryList;
    }
//    public CategoryAdapter getCategoryAdapter(){
//        return categoryAdapter;
//    }
    public void setTaskList(List<TaskModel> newList){
        taskList.setValue(newList);
        updateFirstTask();
    }
    public void updateFirstTask(){
        List<TaskModel> currentList = taskList.getValue();
        if (currentList != null && !currentList.isEmpty()) {
            firstTask.setValue(currentList.get(0));
        }
    }

    //Task getters and setters
    public MutableLiveData<String> getTaskTitle(){
        return taskTitle;
    }
    public MutableLiveData<String> getSessionCount(){
        return sessionCount;
    }
    public MutableLiveData<Integer> getPriorityLevel(){
        return priorityLevel;
    }
    public MutableLiveData<Integer> getSelectedPriority(){
        return selectedPriority;
    }
    public void setPriority(int priority, int radioBtnId){
        priorityLevel.setValue(priority);
        selectedPriority.setValue(radioBtnId);
    }
    public MutableLiveData<String> getTaskDescription(){
        return taskDescription;
    }

    //Category getters and setters
    public MutableLiveData<String> getCategoryTitle(){
        return categoryTitle;
    }
    public MutableLiveData<Integer> getCategoryIcon(){
        return categoryIcon;
    }
    public void setCategoryIcon(Integer icon) {
        categoryIcon.setValue(icon);
    }
    public MutableLiveData<CategoryModel> getCategory(){
        return category;
    }
    public void setCategory(CategoryModel category){
        this.category.setValue(category);
    }

    //THIS IS FOR TESTING PURPOSES ONLY
    //LATER USE FOR ACTUAL DATA FROM DATABASE FIREBASE
    public void initializeTasks(){
        if (testTasks.isEmpty()) {
            testTasks.add(new TaskModel("Task 1", 4, 1, "", "eyo"));
            taskList.setValue(testTasks);
            //adapter.setTasks(testTasks);
        }
        //Log.d("TaskViewModel", "TEST INITIALIZE TASK!");
    }
    public void initializeCategories(){
        if (testCategories.isEmpty()) {
            testCategories.add(new CategoryModel("Work", R.drawable.ic_category_laptop));
            categoryList.setValue(testCategories);
            //categoryAdapter.setCategoryList(testCategories);
        }
    }

    public void clearTaskFields(){
        taskTitle.setValue("");
        sessionCount.setValue("");
        priorityLevel.setValue(1);
        taskDescription.setValue("");
        category.setValue(null);
    }

    public void clearCategoryFields(){
        categoryTitle.setValue("");
        categoryIcon.setValue(0);
    }

    public void addTask(String taskTitle, int sessionCount, int priorityLevel, String categoryTitle, String taskDescription){
        testTasks.add(new TaskModel(taskTitle, sessionCount, priorityLevel, categoryTitle, taskDescription));
        taskList.setValue(testTasks);
        //adapter.setTasks(testTasks);
    }

    public void addCategory(String categoryTitle, Integer icon){
        testCategories.add(new CategoryModel(categoryTitle, icon));
        categoryList.setValue(testCategories);
        //categoryAdapter.setCategoryList(testCategories);
    }

}
