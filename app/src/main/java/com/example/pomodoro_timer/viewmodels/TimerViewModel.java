package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.model.PomodoroLogModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimerViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private CountDownTimer countDownTimer;
    private long totalTime = 10 * 1000; // 25 * 60 * 1000 for 25 minutes
    private long remainingTime = totalTime;
    private boolean isRunning = false;
    private ImageView startBtnIcon;
    private final MutableLiveData<Boolean> sessionFinished = new MutableLiveData<>(false);

    //Getters and setters
    public long getTotalTime() {
        return totalTime;
    }
    public long getRemainingTime() {
        return remainingTime;
    }
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        timerText.setValue(formatTime(totalTime));
    }
    public void setStartBtnIcon(ImageView startBtnIcon) {
        this.startBtnIcon = startBtnIcon;
    }

    //Getters and setters for live data
    private final MutableLiveData<Float> progressAngle = new MutableLiveData<>(360f);
    public MutableLiveData<Float> getProgressAngle() {
        return progressAngle;
    }
    private final MutableLiveData<String> timerText = new MutableLiveData<>(formatTime(totalTime));
    public MutableLiveData<String> getTimerText() {
        return timerText;
    }
    public LiveData<Boolean> getSessionFinished() {
        return sessionFinished;
    }

    //Format the time to display
    private String formatTime(long milliS){
        int minutes = (int) (milliS / 1000) / 60;
        int seconds = (int) (milliS / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(remainingTime, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                float angle = ((float) millisUntilFinished / totalTime) * 360f;
                progressAngle.setValue(angle);
                timerText.setValue(formatTime(remainingTime));
                //Log.d("CHEK MILLIS", "MILLIS: " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                remainingTime = totalTime;
                progressAngle.setValue(360f);
                timerText.setValue(formatTime(remainingTime));
                startBtnIcon.setImageResource(R.drawable.ic_start);
                sessionFinished.setValue(true);
            }
        };
        countDownTimer.start();
    }//End of startTimer method

    public TimerViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
    }

    //Timer functions
    public void startOrResumeTimer(){
        if(isRunning) return;
        isRunning = true;
        startTimer();
    }

    public void stopTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            isRunning = false;
            remainingTime = totalTime;
            progressAngle.setValue(360f);
            timerText.setValue(formatTime(remainingTime));
        }
    }

    public void pauseTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    public void clearSessionFinished() {
        sessionFinished.setValue(false);
    }

    //Database functions
    public void insert(PomodoroLogModel newLog) {
        executor.execute(() -> {
            PomodoroLogModel existingLog = database.pomodoroLogDao().getLogForUserAndDate(newLog.getUserId(), newLog.getTimestamp());

            if (existingLog != null) {
                existingLog.setSessionCount(existingLog.getSessionCount() + 1);
                database.pomodoroLogDao().update(existingLog);
            }else {
                database.pomodoroLogDao().insert(newLog);
            }
        });
    }//End of insert method

}
