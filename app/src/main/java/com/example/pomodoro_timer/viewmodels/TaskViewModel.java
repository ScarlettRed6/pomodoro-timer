package com.example.pomodoro_timer.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.utils.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {

    //Fields
    private List<TaskModel> testTasks;
    private TaskAdapter adapter = new TaskAdapter();
    private MutableLiveData<List<TaskModel>> taskList = new MutableLiveData<>(new ArrayList<>());

    //Getters and Setters
    public LiveData<List<TaskModel>> getTaskList(){
        return taskList;
    }
    public TaskAdapter getAdapter(){
        return adapter;
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

    public void addTask(TaskModel task){
        testTasks.add(task);
        taskList.setValue(testTasks);
        adapter.setTasks(testTasks);
    }

}
