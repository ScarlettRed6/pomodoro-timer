package com.example.pomodoro_timer.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.StatsModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StatsViewModel extends AndroidViewModel {


    private final AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public StatsViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
    }

    public void insert(StatsModel stats) {
        executor.execute(() -> database.statsDao().insert(stats));
    }

    public void update(StatsModel stats) {
        executor.execute(() -> database.statsDao().update(stats));
    }

    public void delete(StatsModel stats) {
        executor.execute(() -> database.statsDao().delete(stats));
    }

    public LiveData<StatsModel> getStatsById(int id) {
        return database.statsDao().getStatsById(id);
    }

    public LiveData<List<StatsModel>> getStatsByTask(int taskId) {
        return database.statsDao().getStatsByTask(taskId);
    }

    public LiveData<List<StatsModel>> getAllStats() {
        return database.statsDao().getAllStats();
    }
}
