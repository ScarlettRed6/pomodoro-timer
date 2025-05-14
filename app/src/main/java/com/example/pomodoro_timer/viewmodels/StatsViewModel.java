package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.model.StatsModel;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StatsViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Integer> productivityScore = new MutableLiveData<>(0);
    private final MutableLiveData<Double> totalFocus = new MutableLiveData<>();
    private final MutableLiveData<Long> breakTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> pomodoroSessions = new MutableLiveData<>();
    private final MutableLiveData<Integer> taskID = new MutableLiveData<>();
    private final MutableLiveData<List<PomodoroLogModel>> pomodoroLogs = new MutableLiveData<>();

    //Getters and setters
    public MutableLiveData<Integer> getProductivityScore() {
        return productivityScore;
    }
    public LiveData<Double> getTotalFocus() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalFocus.setValue(Double.parseDouble(decimalFormat.format(totalFocus.getValue())));
        return totalFocus;
    }
    public MutableLiveData<Long> getBreakTime() {
        return breakTime;
    }
    public LiveData<Integer> getPomodoroSessions() {
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
    public void setPomodoroSessions(int sessions) {
        pomodoroSessions.setValue(sessions);
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

    public void updateStats(int userId) {
        resetStats();
        executor.execute(() -> {
            int totalPomodoros = database.pomodoroLogDao().getTotalPomodoroSessions(userId);

            StatsModel stats = database.statsDao().getStatsByUserRaw(userId);
            if (stats != null) {
                calculateProductivityScore(totalPomodoros);
                stats.setTotalPomodoroSessions(totalPomodoros);
                stats.setProductivityScore(productivityScore.getValue());
                database.statsDao().update(stats);
            } else {
                //if no record exists, insert one
                stats = new StatsModel();
                stats.setUserID(userId);
                stats.setTotalPomodoroSessions(totalPomodoros);
                database.statsDao().insert(stats);
            }

            //Also update the LiveData
            totalFocus.postValue(stats.getTotalFocus());
            pomodoroSessions.postValue(totalPomodoros);
            Log.d("CHECK POMODOROS","POMODOROS SESSIONS: " + pomodoroSessions.getValue());
        });
    }//End of updateStats method

    public void resetStats() {
        totalFocus.setValue(0.0);
        pomodoroSessions.setValue(0);
        productivityScore.setValue(0);
        breakTime.setValue(0L);
    }//End of resetStats method

    private void calculateProductivityScore(int totalPomodoros) {
        //Implement later
    }//End of calculateProductivityScore method

}
