package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.CategoryModel;
import com.example.pomodoro_timer.model.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase db;
    private final ExecutorService executor;
    private final List<TaskModel> testTasks = new ArrayList<>();
    private final List<CategoryModel> testCategories = new ArrayList<>();
    //private CategoryAdapter categoryAdapter = new CategoryAdapter();
    //private TaskAdapter adapter = new TaskAdapter();

    //Task list
    private final MutableLiveData<List<TaskModel>> taskList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<TaskModel> firstTask = new MutableLiveData<>();
    private final MutableLiveData<List<CategoryModel>> categoryList = new MutableLiveData<>(new ArrayList<>());

    //Task fields
    private final MutableLiveData<String> taskTitle = new MutableLiveData<>("");
    private final MutableLiveData<String> sessionCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> priorityLevel = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> selectedPriority = new MutableLiveData<>();
    private final MutableLiveData<String> taskDescription = new MutableLiveData<>("");
    private final MutableLiveData<Integer> taskId = new MutableLiveData<>();

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
    public MutableLiveData<Integer> getTaskId(){
        return taskId;
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

    //Constructor
    public TaskViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        executor = Executors.newSingleThreadExecutor();
    }

    //THIS IS FOR GUEST USER
    public void initializeTasks(){
        executor.execute(() -> {
            List<TaskModel> currentTasks = db.taskDao().getAll(1);
            int newPosition = currentTasks.size();

            if (!db.userDao().hasTask(1)){
                TaskModel guestTask = new TaskModel(1, "Goon", 1, 1, 1, "Test Description");
                guestTask.setPosition(newPosition);

                db.taskDao().insert(guestTask);
                taskList.postValue(db.taskDao().getAll(1));
            }
        });
        //Log.d("TaskViewModel", "TEST INITIALIZE TASK!");
    }//End of initializeTasks method

    public void initializeCategories(){
        executor.execute(() -> {
            if (!db.userDao().hasCategory(1)){
                CategoryModel guestCategory = new CategoryModel(1, "Work", R.drawable.ic_category_laptop);
                db.categoryDao().insert(guestCategory);
                categoryList.postValue(db.categoryDao().getAllCategories(1));
            }
        });
    }//End of initializeCategories method

    public void resetToTestData() {
        testTasks.clear();
        testCategories.clear();
        initializeTasks();
        initializeCategories();
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

    public void addTask(int userId, String taskTitle, int sessionCount, int priorityLevel, int categoryId, String taskDescription){
        executor.execute(() -> {
            List<TaskModel> currentTasks = db.taskDao().getAll(userId);
            int newPosition = currentTasks.size();

            TaskModel newTask = new TaskModel(userId, taskTitle, sessionCount, priorityLevel, categoryId, taskDescription);
            newTask.setPosition(newPosition);

            db.taskDao().insert(newTask);
            taskList.postValue(db.taskDao().getAll(userId));
        });
    }//End of addTask for logged in user method\

    public void updateTask(int userId, int taskId, String taskTitle, int sessionCount, int priorityLevel, int categoryId, String taskDescription){
        executor.execute(() -> {
            TaskModel task = db.taskDao().getTaskById(userId, taskId);
            task.setTaskTitle(taskTitle);
            task.setSessionCount(sessionCount);
            task.setPriorityLevel(priorityLevel);
            task.setCategoryId(categoryId);
            task.setTaskDescription(taskDescription);
            db.taskDao().update(task);
        });
    }

    public void deleteTask(TaskModel task){
        executor.execute(() -> {
            db.taskDao().delete(task);
            List<TaskModel> allTasks = db.taskDao().getAll(task.getUserId());
            Log.d("LOG_CHECK_TASK_FROM_DB", allTasks != null ? allTasks.toString() : "No tasks found");
            taskList.postValue(allTasks);
        });
    }//End of deleteTask method

    public void updateTaskOrder(List<TaskModel> tasks) {
        executor.execute(() -> {
            db.taskDao().updateTasks(tasks);
        });
    }

    public void loadEditTask(TaskModel task){
        taskTitle.setValue(task.getTaskTitle());
        taskDescription.setValue(task.getTaskDescription());
        sessionCount.setValue(String.valueOf(task.getSessionCount()));
        priorityLevel.setValue(task.getPriorityLevel()); //Set initial priority
        selectedPriority.setValue(getRadioButtonIdForPriority(task.getPriorityLevel()));
        taskId.setValue(task.getId());
        matchCategory(task.getCategoryId());
    }//End of loadEditTask method

    private void matchCategory(int categoryId){
        executor.execute(() -> {
            CategoryModel category = db.categoryDao().getCategoryById(categoryId);
            this.category.postValue(category);
        });
    }//End of matchCategory method

    private int getRadioButtonIdForPriority(int priority) {
        switch (priority) {
            case 1:
                return R.id.priority_high;
            case 2:
                return R.id.priority_medium;
            case 3:
                return R.id.priority_low;
            default:
                return -1;
        }
    }

    public void displayTask(int userId){
        executor.execute(() -> {
            List<TaskModel> allTasks = db.taskDao().getAll(userId);
            Log.d("LOG_CHECK_TASK_FROM_DB", allTasks != null ? allTasks.toString() : "No tasks found");
            taskList.postValue(allTasks);
        });
    }//End of displayTask method

    public void displayCategory(int userId){
        executor.execute(() -> {
            List<CategoryModel> allCategories = db.categoryDao().getAllCategories(userId);
            Log.d("LOG_CHECK_CATEGORY_FROM_DB", allCategories != null ? allCategories.toString() : "No categories found");
            categoryList.postValue(allCategories);
        });
    }

    public void addCategory(int userId, String categoryTitle, Integer icon){
        executor.execute(() -> {
            CategoryModel newCategory = new CategoryModel(userId, categoryTitle, icon);
            db.categoryDao().insert(newCategory);
            categoryList.postValue(db.categoryDao().getAllCategories(userId)); //Refresh category list after insert
        });
    }

    public void deleteCategory(CategoryModel category) {

    }


}
