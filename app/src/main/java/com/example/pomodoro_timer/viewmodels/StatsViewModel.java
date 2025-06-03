package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.DateTaskCount;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.model.StatsModel;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StatsViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private int targetSessions = 12;
    private final MutableLiveData<Integer> productivityScore = new MutableLiveData<>(0);
    private final MutableLiveData<Double> totalFocus = new MutableLiveData<>();
    private final MutableLiveData<Double> breakTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> pomodoroSessions = new MutableLiveData<>();
    private final MutableLiveData<Integer> taskID = new MutableLiveData<>();
    private final MutableLiveData<List<PomodoroLogModel>> pomodoroLogs = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentFilteredSessions = new MutableLiveData<>(0);
    private final MutableLiveData<String> currentFilter = new MutableLiveData<>("All");
    private final MutableLiveData<String> loggedEmail = new MutableLiveData<>("User");

    //Getters and setters
    public MutableLiveData<Integer> getProductivityScore() {
        return productivityScore;
    }
    public LiveData<Integer> getCurrentFilteredSessions() {
        return currentFilteredSessions;
    }

    public MutableLiveData<String> getCurrentFilter() {
        return currentFilter;
    }
    public LiveData<Double> getTotalFocus() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalFocus.setValue(Double.parseDouble(decimalFormat.format(totalFocus.getValue())));
        return totalFocus;
    }
    public MutableLiveData<Double> getBreakTime() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        breakTime.setValue(Double.parseDouble(decimalFormat.format(breakTime.getValue())));
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
    public void setLoggedEmail(String email) {
        loggedEmail.setValue(email);
    }
    public LiveData<String> getLoggedEmail() {
        return loggedEmail;
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
            breakTime.postValue(stats.getBreakTime());
            pomodoroSessions.postValue(totalPomodoros);
            Log.d("CHECK POMODOROS","POMODOROS SESSIONS: " + pomodoroSessions.getValue());
        });
    }//End of updateStats method

    public void resetStats() {
        totalFocus.setValue(0.0);
        pomodoroSessions.setValue(0);
        productivityScore.setValue(0);
        breakTime.setValue(0.0);
    }//End of resetStats method

    private void calculateProductivityScore(int totalPomodoros) {
        String filter = currentFilter.getValue();
        int targetSessions = getTargetSessionsForFilter(filter);

        int score = 0;
        if (targetSessions > 0) {
            score = Math.min(100, (totalPomodoros * 100) / targetSessions);
        }

        productivityScore.postValue(score);
    }//End of calculateProductivityScore method

    private int getTargetSessionsForFilter(String filter) {
        switch (filter) {
            case "Week":
                return targetSessions; // 8 sessions per week
            case "Month":
                return targetSessions * 4; // 32 sessions per month (8 * 4 weeks)
            case "Year":
                return targetSessions * 52; // 416 sessions per year (8 * 52 weeks)
            case "All":
                // For "All", calculate based on days since first log
                // You might want to adjust this logic based on your needs
                return targetSessions * 4; // Default to monthly target
            default:
                return targetSessions;
        }
    }

    // Add this new method to update productivity for filtered data
    public void updateProductivityForFilter(String filter, int filteredSessions) {
        currentFilter.setValue(filter);
        currentFilteredSessions.setValue(filteredSessions);

        int targetSessions = getTargetSessionsForFilter(filter);
        int score = 0;
        if (targetSessions > 0) {
            score = Math.min(100, (filteredSessions * 100) / targetSessions);
        }

        productivityScore.postValue(score);
    }

    public LiveData<Map<String, Integer>> getHeatMapDataForMonth(int userId, int month, int year) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, month, 1, 0, 0, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        long startTime = startCal.getTimeInMillis();

        Calendar endCal = Calendar.getInstance();
        endCal.set(year, month, startCal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        long endTime = endCal.getTimeInMillis();

        return Transformations.map(
                database.completedTaskLogDao().observeCompletedTaskCountsByDateRange(userId, startTime, endTime),
                list -> {
                    Map<String, Integer> map = new HashMap<>();
                    for (DateTaskCount entry : list) {
                        map.put(entry.date, entry.completedCount);
                    }
                    return map;
                }
        );
    }//End of getHeatMapDataForMonth method

    public LiveData<Map<String, Integer>> getHeatMapData(int userId) {
        return Transformations.map(database.completedTaskLogDao().observeCompletedTaskCountsByDate(userId), list -> {
            Map<String, Integer> map = new HashMap<>();
            for (DateTaskCount entry : list) {
                map.put(entry.date, entry.completedCount);
            }
            return map;
        });
    }//End of getHeatMapData method

}