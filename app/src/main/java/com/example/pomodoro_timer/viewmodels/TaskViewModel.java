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
import com.example.pomodoro_timer.model.CompletedTaskLogModel;
import com.example.pomodoro_timer.model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase db;
    private final ExecutorService executor;
    private final List<TaskModel> testTasks = new ArrayList<>();
    private final List<CategoryModel> testCategories = new ArrayList<>();

    //Task list
    private final MutableLiveData<List<TaskModel>> taskList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<TaskModel>> finishedTaskList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<TaskModel>> inProgressTaskList = new MutableLiveData<>(new ArrayList<>());
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
    private MutableLiveData<String> categoryDescription = new MutableLiveData<>("");
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
    public MutableLiveData<List<TaskModel>> getFinishedTaskList() {
        return finishedTaskList;
    }
    public MutableLiveData<List<TaskModel>> getInProgressTaskList() {
        return inProgressTaskList;
    }

    //Category getters and setters
    public MutableLiveData<String> getCategoryTitle(){
        return categoryTitle;
    }
    public MutableLiveData<Integer> getCategoryIcon(){
        return categoryIcon;
    }
    public MutableLiveData<String> getCategoryDescription(){
        return categoryDescription;
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
                TaskModel guestTask = new TaskModel(1, "Work", 1, 1, 1, "Test Description");
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
                CategoryModel guestCategory = new CategoryModel(1, "Work","this Category", R.attr.categoryLaptopIcon);
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
        categoryDescription.setValue("");
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

            refreshCategoryProgress(userId);
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

            refreshCategoryProgress(task.getUserId());
        });
    }//End of deleteTask method

    public void deleteInProgressTask(TaskModel task){
        executor.execute(() -> {
            db.taskDao().delete(task);
            List<TaskModel> allTasks = db.taskDao().getAll(task.getUserId());
            Log.d("LOG_CHECK_TASK_FROM_DB", allTasks != null ? allTasks.toString() : "No tasks found");
            inProgressTaskList.postValue(allTasks);
        });
    }//End of deleteInProgressTask method

    public void reAddTask(TaskModel task){
        executor.execute(() -> {
            //This updates the task to not be completed
            task.setIsCompleted(false);
            task.setSessionsCompleted(0);
            task.setTimeFinished(0L);
            db.taskDao().update(task);
        });
    }//End of reAddTask method

    public void updateTaskOrder(List<TaskModel> tasks) {
        executor.execute(() -> {
            db.taskDao().updateTasks(tasks);
        });
    }//End of updateTaskOrder method

    public void loadEditTask(TaskModel task){
        taskTitle.setValue(task.getTaskTitle());
        taskDescription.setValue(task.getTaskDescription());
        sessionCount.setValue(String.valueOf(task.getSessionCount()));
        priorityLevel.setValue(task.getPriorityLevel()); //Set initial priority
        //selectedPriority.setValue(getRadioButtonIdForPriority(task.getPriorityLevel()));
        taskId.setValue(task.getId());
        matchCategory(task.getCategoryId());
    }//End of loadEditTask method

    public void loadEditCategory(CategoryModel category){
        categoryTitle.setValue(category.getCategoryTitle());
        categoryIcon.setValue(category.getIcon());
        if (!category.getCategoryDescription().isEmpty()){
            categoryDescription.setValue(category.getCategoryDescription());
        }else {
            categoryDescription.setValue("");
        }
        Log.d("CHECK_DESC", category.getCategoryDescription());
        this.category.setValue(category);
    }//End of loadEditCategory method

    private void matchCategory(int categoryId){
        executor.execute(() -> {
            CategoryModel category = db.categoryDao().getCategoryById(categoryId);
            this.category.postValue(category);
        });
    }//End of matchCategory method

//    private int getRadioButtonIdForPriority(int priority) {
//        switch (priority) {
//            case 1:
//                return R.id.priority_high;
//            case 2:
//                return R.id.priority_medium;
//            case 3:
//                return R.id.priority_low;
//            default:
//                return -1;
//        }
//    }//End of getRadioButtonIdForPriority method

    public void displayTask(int userId){
        executor.execute(() -> {
            List<TaskModel> allTasks = db.taskDao().getAll(userId);
            Log.d("LOG_CHECK_TASK_FROM_DB", allTasks != null ? allTasks.toString() : "No tasks found");
            taskList.postValue(allTasks);
        });
    }//End of displayTask method

    public void displayOngoingTask(int userId){
        executor.execute(() -> {
            List<TaskModel> ongoingTasks = db.taskDao().getAllOngoingTasks(userId);
            Log.d("LOG_CHECK_TASK_FROM_DB", ongoingTasks != null ? ongoingTasks.toString() : "No tasks found");
            inProgressTaskList.postValue(ongoingTasks);
        });
    }//End of displayOngoingTask method

    public void displayCompletedTask(int userId) {
        executor.execute(() -> {
            List<TaskModel> completedTasks = db.taskDao().getAllCompletedTasks(userId);
            Log.d("LOG_CHECK_TASK_FROM_DB", completedTasks != null ? completedTasks.toString() : "No tasks found");
            finishedTaskList.postValue(completedTasks);
        });
    }//End of displayCompletedTask method

    public void displayCategory(int userId){
        displayCategoryWithProgress(userId);
    }

    public void displayTaskByCategory(int userId, int categoryId){
        executor.execute(() -> {
            List<TaskModel> viewTaskCategories = db.taskDao().getAllTaskByCategory(userId, categoryId);
            taskList.postValue(viewTaskCategories);
        });
    }//End of displayTaskByCategry method

    public void removeCategoryOfTask(int userId, int categoryId, int taskId){
        executor.execute(() -> {
            db.taskDao().removeCategoryOfTask(userId, categoryId, taskId);
            displayTaskByCategory(userId, categoryId);
        });
    }//End of removeCategoryOfTask method

    public void addCategory(int userId, String categoryTitle, String categoryDescription, Integer icon){
        executor.execute(() -> {
            CategoryModel newCategory = new CategoryModel(userId, categoryTitle, categoryDescription, icon);
            db.categoryDao().insert(newCategory);
            Log.d("TaskViewModel_AddCat", "Adding category. User: " + userId + ", Title: " + categoryTitle + ", IconAttrID: " + icon);
            categoryList.postValue(db.categoryDao().getAllCategories(userId)); //Refresh category list after insert
        });
    }

    public void updateCategory(int userId, int categoryId, String categoryTitle, int categoryIcon, String categoryDescription){
        executor.execute(() -> {
            CategoryModel category = db.categoryDao().getCategoryById(categoryId);
            category.setCategoryTitle(categoryTitle);
            category.setIcon(categoryIcon);
            category.setCategoryDescription(categoryDescription);
            db.categoryDao().update(category);
        });
    }//End of updateCategory method

    public void reApplyCategoryTasks(int userId, int categoryId){
        executor.execute(() -> {
            //This reapplys the tasks of the category where the tasks are completed and sets it to not completed and resets the remaining sessions
            List<TaskModel> tasks = db.taskDao().getAllCompletedTasksByCategory(userId, categoryId);
            for (TaskModel task : tasks) {
                task.setIsCompleted(false);
                task.setSessionsCompleted(0);
                task.setTimeFinished(0L);
                db.taskDao().update(task);
            }
            displayTaskByCategory(userId, categoryId);
        });
    }//End of reApplyCategoryTasks method

    public void clearAllFinishedTasks(int userId){
        executor.execute(() -> {
            List<TaskModel> tasks = db.taskDao().getAllCompletedTasks(userId);
            for (TaskModel task : tasks) {
                db.taskDao().delete(task);
            }
        });
    }//End of clearAllFinishedTasks method

    public void deleteCategory(CategoryModel category) {
        executor.execute(() -> {
            db.categoryDao().delete(category);
            List<CategoryModel> allCategories = db.categoryDao().getAllCategories(category.getUserId());
            categoryList.postValue(allCategories);
        });
    }//End of deleteCategory method

    public void increaseFinishedTaskSession(TaskModel task) {
        if (task == null) return;

        executor.execute(() -> {
            //Get the latest task data from database
            TaskModel currentTask = db.taskDao().getTaskById(task.getUserId(), task.getId());
            if (currentTask == null) return;

            int newRemainingSessions = currentTask.getSessionsCompleted() + 1;

            //Ensure remaining sessions don't go below 0
            if (newRemainingSessions == currentTask.getSessionCount()) {
                newRemainingSessions = currentTask.getSessionCount();
            }

            //This also updates the UI
            currentTask.setSessionsCompleted(newRemainingSessions);

            //If no remaining sessions, mark as completed and set completion date
            if (newRemainingSessions == currentTask.getSessionCount()) {
                currentTask.setIsCompleted(true);
                long completionTime = System.currentTimeMillis();
                currentTask.setTimeFinished(completionTime);

                logCompletedTask(currentTask.getUserId(), completionTime);

                Log.d("LOG_TASK_COMPLETED", "Task completed: " + currentTask.getTaskTitle());
            }

            //Update the task in database
            db.taskDao().update(currentTask);

            //Refresh the task list to update UI
            List<TaskModel> allTasks = db.taskDao().getAll(currentTask.getUserId());
            taskList.postValue(allTasks);

            refreshCategoryProgress(currentTask.getUserId());

            Log.d("LOG_TASK_SESSION_UPDATE",
                    "Task: " + currentTask.getTaskTitle() +
                            ", Remaining sessions: " + newRemainingSessions +
                            ", Completed: " + currentTask.getIsCompleted());
        });
    }//End of decreaseTaskSession method

    private void logCompletedTask(int userId, long completionTime) {
        executor.execute(() -> {
            // Format date as YYYY-MM-DD
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String completionDate = dateFormat.format(new Date(completionTime));

            // Check if log already exists for this user and date
            CompletedTaskLogModel existingLog = db.completedTaskLogDao().getLogByUserAndDate(userId, completionDate);

            if (existingLog != null) {
                // Update existing log by incrementing count
                existingLog.setCompletedCount(existingLog.getCompletedCount() + 1);
                existingLog.setTimestamp(completionTime); // Update timestamp to latest completion
                db.completedTaskLogDao().update(existingLog);
                Log.d("LOG_COMPLETED_TASK", "Updated existing log for " + completionDate + ", new count: " + existingLog.getCompletedCount());
            } else {
                // Create new log entry
                CompletedTaskLogModel newLog = new CompletedTaskLogModel(userId, completionDate, 1, completionTime);
                db.completedTaskLogDao().insert(newLog);
                Log.d("LOG_COMPLETED_TASK", "Created new log for " + completionDate);
            }
        });
    }

    public void displayCategoryWithProgress(int userId){
        executor.execute(() -> {
            List<CategoryModel> allCategories = db.categoryDao().getAllCategories(userId);

            // Calculate progress for each category
            for (CategoryModel category : allCategories) {
                int totalTasks = db.categoryDao().getTotalTasksInCategory(userId, category.getId());
                int completedTasks = db.categoryDao().getCompletedTasksInCategory(userId, category.getId());

                category.setTotalTasks(totalTasks);
                category.setCompletedTasks(completedTasks);
            }

            Log.d("LOG_CHECK_CATEGORY_FROM_DB", allCategories != null ? allCategories.toString() : "No categories found");
            categoryList.postValue(allCategories);
        });
    }

    private void refreshCategoryProgress(int userId) {
        displayCategoryWithProgress(userId);
    }

}
