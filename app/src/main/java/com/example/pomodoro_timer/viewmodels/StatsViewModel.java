package com.example.pomodoro_timer.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.model.StatsModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StatsViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Integer> productivityScore = new MutableLiveData<>();
    private final MutableLiveData<Long> totalFocus = new MutableLiveData<>();
    private final MutableLiveData<Long> breakTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> pomodoroSessions = new MutableLiveData<>();
    private final MutableLiveData<Integer> taskID = new MutableLiveData<>();
    private final MutableLiveData<List<PomodoroLogModel>> pomodoroLogs = new MutableLiveData<>();

    //Getters and setters
    public MutableLiveData<Integer> getProductivityScore() {
        return productivityScore;
    }
    public MutableLiveData<Long> getTotalFocus() {
        return totalFocus;
    }
    public MutableLiveData<Long> getBreakTime() {
        return breakTime;
    }
    public MutableLiveData<Integer> getPomodoroSessions() {
        return pomodoroSessions;
    }
    public MutableLiveData<Integer> getTaskID() {
        return taskID;
    }
    public LiveData<StatsModel> getStatsById(int id) {
        return database.statsDao().getStatsById(id);
    }

    public LiveData<List<StatsModel>> getAllStats() {
        return database.statsDao().getAllStats();
    }
    public LiveData<List<PomodoroLogModel>> getPomodoroLogs(int userId, long start, long end) {
        return database.pomodoroLogDao().getLogsBetween(userId, start, end);
    }

    //Constructor
    public StatsViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
    }//End of constructor

    public void insert(StatsModel stats) {
        executor.execute(() -> database.statsDao().insert(stats));
    }

    public void update(StatsModel stats) {
        executor.execute(() -> database.statsDao().update(stats));
    }

    public void delete(StatsModel stats) {
        executor.execute(() -> database.statsDao().delete(stats));
    }

}
