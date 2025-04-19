package com.example.pomodoro_timer.viewmodels;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro_timer.R;

public class TimerViewModel extends ViewModel {

    //Fields
    private CountDownTimer countDownTimer;
    private long totalTime = 10 * 1000; // 25 * 60 * 1000 for 25 minutes
    private long remainingTime = totalTime;
    private boolean isRunning = false;
    private ImageView startBtnIcon;

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
            }
        };
        countDownTimer.start();
    }//End of startTimer method

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

}
