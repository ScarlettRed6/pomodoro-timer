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
import com.example.pomodoro_timer.model.StatsModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimerViewModel extends AndroidViewModel {

    //Fields
    private final AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private CountDownTimer countDownTimer;
    private long totalTime = 10 * 1000; // 25 * 60 * 1000 for 25 minutes // this is also for the pomodoro default
    private long remainingTime = totalTime;
    private boolean isRunning = false;
    private boolean isUserLoggedIn = false;
    private boolean sessionStartedWhileLoggedIn = false;
    private final MutableLiveData<Integer> defaultLBInterval = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> currentLongBreakInterval = new MutableLiveData<>(defaultLBInterval.getValue());
    private final MutableLiveData<Boolean> sessionFinished = new MutableLiveData<>(false);
    private final MutableLiveData<Float> progressAngle = new MutableLiveData<>(360f);
    private final MutableLiveData<String> timerText = new MutableLiveData<>(formatTime(totalTime));
    private final MutableLiveData<String> timerTypeText = new MutableLiveData<>("Pomodoro");

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
    public void setUserLoggedIn(boolean loggedIn) {
        this.isUserLoggedIn = loggedIn;
        //Reset session tracking state when login state changes
        if (!loggedIn) {
            sessionStartedWhileLoggedIn = false;
        }
    }

    //Getters and setters for live data
    public MutableLiveData<Float> getProgressAngle() {
        return progressAngle;
    }
    public MutableLiveData<String> getTimerText() {
        return timerText;
    }
    public LiveData<Boolean> getSessionFinished() {
        return sessionFinished;
    }
    public LiveData<String> getTimerTypeText(){
        return timerTypeText;
    }
    public LiveData<Integer> getDefaultLBInterval(){
        return defaultLBInterval;
    }
    public void setDefaultLBInterval(int defInterval){
        this.defaultLBInterval.setValue(defInterval);
    }
    public LiveData<Integer> getLongBreakInterval(){
        return currentLongBreakInterval;
    }
    public void setLongBreakInterval(int interval){
        this.currentLongBreakInterval.setValue(interval);
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
                setTimerType();
                remainingTime = totalTime;
                progressAngle.setValue(360f);
                timerText.setValue(formatTime(totalTime));
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
        sessionStartedWhileLoggedIn = isUserLoggedIn;
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

    public boolean wasSessionStartedWhileLoggedIn() {
        return sessionStartedWhileLoggedIn;
    }

    //Database functions
    public void insertOrUpdate(PomodoroLogModel newLog) {
        executor.execute(() -> {
            PomodoroLogModel existingLog = database.pomodoroLogDao().getLogForUserAndDate(newLog.getUserId(), newLog.getTimestamp());

            if (existingLog != null) {
                if (newLog.getSessionCount() > 0) {
                    existingLog.setSessionCount(existingLog.getSessionCount() + newLog.getSessionCount());
                }

                //Optionally, also accumulate focus/break time here if needed
                existingLog.setFocusTime(existingLog.getFocusTime() + newLog.getFocusTime());

                database.pomodoroLogDao().update(existingLog);
            } else {
                database.pomodoroLogDao().insert(newLog);
            }
        });
    }//End of insert method

    public void recordPomodoroSession(int userId, long focusTimeMillis) {
        long timestamp = System.currentTimeMillis();
        PomodoroLogModel log = new PomodoroLogModel(userId, timestamp, 1, focusTimeMillis);
        insertOrUpdate(log);
    }

    public void recordBreakSession(int userId, long breakTimeMillis) {
        long timestamp = System.currentTimeMillis();
        PomodoroLogModel log = new PomodoroLogModel(userId, timestamp, breakTimeMillis);
        insertOrUpdate(log);
    }

    public void saveTotalFocus(int userId, long sessionDuration) {
        if (userId <= 0) return;

        double hours = (double) sessionDuration / (1000 * 60 * 60);

        executor.execute(() -> {
            StatsModel stats = database.statsDao().getStatsByUserRaw(userId);
            if (stats != null) {
                double newTotalFocus = stats.getTotalFocus() + hours;
                stats.setTotalFocus(newTotalFocus);
                database.statsDao().update(stats);

                Log.d("CHECK TOTAL FOCUS", "TOTAL FOCUS: " + newTotalFocus);
            } else {
                Log.w("saveTotalFocus", "No StatsModel found for userId: " + userId);
            }
        });
    }//End of saveTotalFocus method

    public void saveTotalBreakTime(int userId, long breakDuration){
        if (userId <= 0) return;
        double hours = (double) breakDuration / (1000 * 60 * 60);

        executor.execute(() -> {
            StatsModel stats = database.statsDao().getStatsByUserRaw(userId);
            if (stats != null) {
                double newBreakTime = stats.getBreakTime() + hours;
                stats.setBreakTime(newBreakTime);
                database.statsDao().update(stats);
                Log.d("LOG_CHECK_BREAK_TIME", "BREAK TIME: " + newBreakTime);
            }else {
                Log.w("LOG_CHECK_BREAK_TIME_NO_USER", "No StatsModel found for userId: " + userId);
            }
        });
    }//End of saveTotalBreakTime method

    public void setTimerType(){
        String type = timerTypeText.getValue();
        int currentInterval = currentLongBreakInterval.getValue();
        int defaultInterval = defaultLBInterval.getValue();

        if ("Pomodoro".equals(type)) {
            if (currentInterval > 0) {
                timerTypeText.setValue("Short Break");
                currentLongBreakInterval.setValue(currentInterval - 1);
            } else {
                timerTypeText.setValue("Long Break");
                currentLongBreakInterval.setValue(defaultInterval);
            }
        } else {
            timerTypeText.setValue("Pomodoro");
        }

    }//End of setTimerType method

    public long getTodayMidnightMillis() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(now);
        try {
            Date date = sdf.parse(formattedDate);
            return date != null ? date.getTime() : System.currentTimeMillis();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

}
