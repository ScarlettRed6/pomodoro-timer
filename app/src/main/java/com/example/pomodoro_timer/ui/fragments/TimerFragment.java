package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTimerBinding;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.ui.custom.TimerAnimationView;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;
import com.example.pomodoro_timer.viewmodels.TimerViewModel;

public class TimerFragment extends Fragment {

    //Fields
    private TimerViewModel timerVM;
    private TimerAnimationView timerAnimView;
    private TaskViewModel taskVM;
    private FragmentTimerBinding binding;
    private SettingsViewModel settingsVM;
    private SharedViewModel sharedVM;
    private TaskModel firstTask;
    private Integer userId;
    private Integer pMinutes, sMinutes, lMinutes;
    private Integer pSeconds, sSeconds, lSeconds;
    private String currentType = "Pomodoro";
    private boolean isUserLoggedIn = false;

    public TimerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        timerVM = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.setTimerVM(timerVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Initialize things
        init();

        return binding.getRoot();
    }//End of onCreateView method

    private void init(){
        checkUser();
        timerVM.setUserLoggedIn(isUserLoggedIn);
        taskVM.initializeTasks();
        taskVM.initializeCategories();
        observeTaskList();
        timerListener();
        initializeTimers();
        settingsVM.getPomodoroMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime());
        settingsVM.getPomodoroSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime());
        settingsVM.getShortBreakMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime());
        settingsVM.getShortBreakSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime());
        settingsVM.getLongBreakMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime());
        settingsVM.getLongBreakSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime());

        if (isUserLoggedIn){
            listenSession();
        }
    }//End of init method

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();
        isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();

        if (isUserLoggedIn){
            taskVM.displayTask(userId);
            taskVM.displayCategory(userId);
        }else {
            taskVM.displayTask(1);
            taskVM.displayCategory(1);
        }

    }//End of checkUser method

    private void initializeTimers(){
        //For pomodoro timer
        pMinutes = settingsVM.getPomodoroMinutes().getValue();
        pSeconds = settingsVM.getPomodoroSeconds().getValue();
        //For short break timer
        sMinutes = settingsVM.getShortBreakMinutes().getValue();
        sSeconds = settingsVM.getShortBreakSeconds().getValue();
        //For long break timer
        lMinutes = settingsVM.getLongBreakMinutes().getValue();
        lSeconds = settingsVM.getLongBreakSeconds().getValue();
    }//End of initializeTimers method

    private void updateTimerTotalTime() {
        currentType = timerVM.getTimerTypeText().getValue();

        if ("Pomodoro".equals(currentType) && pMinutes != null && pSeconds != null) {
            long totalTime = (pMinutes * 60L + pSeconds) * 1000L;
            timerVM.setTotalTime(totalTime);
            timerVM.stopTimer();
            Log.d("LOG_CHECK_POMODORO", "TIMER TOTAL TIME: " + totalTime);
        } else if ("Short Break".equals(currentType) && sMinutes != null && sSeconds != null) {
            long totalTime = (sMinutes * 60L + sSeconds) * 1000L;
            timerVM.setTotalTime(totalTime);
            //timerVM.stopTimer();
            Log.d("LOG_CHECK_SHORT_BREAK", "TIMER TOTAL TIME: " + totalTime);
        } else if ("Long Break".equals(currentType) && lMinutes != null && lSeconds != null) {
            long totalTime = (lMinutes * 60L + lSeconds) * 1000L;
            timerVM.setTotalTime(totalTime);
            timerVM.stopTimer();
            Log.d("LOG_CHECK_LONG_BREAK", "TIMER TOTAL TIME: " + totalTime);
        }
    }//End of updateTimerTotalTime method

    private void timerListener(){
        timerAnimView = binding.timerAnimationViewId;

        timerVM.getProgressAngle().observe(getViewLifecycleOwner(), angle -> {
            if(angle != null){
                timerAnimView.setProgress(angle);
                //Log.d("ANGLE", "THIS THANG IS BEING CALLED: TIMERFRAGMENT");
            }
        });

        FrameLayout startBtn = binding.startBtn;
        ImageView startBtnIcon = binding.startBtnIcon;
        FrameLayout stopBtn = binding.stopBtn;
        ImageView stopBtnIcon = binding.stopBtnIcon;

        timerVM.setStartBtnIcon(startBtnIcon);

        binding.startBtnIcon.setImageResource(R.drawable.ic_start);
        startBtn.setOnClickListener(v -> {
            startBtnIcon.animate()
                        .rotation(270)
                        .setDuration(150)
                        .withEndAction(() -> startBtnIcon.animate()
                                .rotation(0)
                                .setDuration(150));
            //End of animation

            if(timerVM.isRunning()){
                timerVM.pauseTimer();
                binding.startBtnIcon.setImageResource(R.drawable.ic_start);
            } else{
                timerVM.startOrResumeTimer();
                binding.startBtnIcon.setImageResource(R.drawable.ic_pause);
            }
        });//End of startBtn listener

        stopBtn.setOnClickListener(v -> {
            stopBtnIcon.animate()
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(150)
                    .withEndAction(() -> stopBtnIcon.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150));
            //End of animation
            timerVM.stopTimer();
            binding.startBtnIcon.setImageResource(R.drawable.ic_start);
        });

    }//End of timerListener method

    private void observeTaskList(){
        taskVM.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            if(tasks != null && !tasks.isEmpty()){
                firstTask = tasks.get(0);
                updateTaskUI();
                binding.firstTaskCardId.setVisibility(View.VISIBLE);
            } else {
                firstTask = null;
                binding.firstTaskCardId.setVisibility(View.GONE);
                Log.d("LOG_NO_TASKS", "No incomplete tasks available");
            }
        });
    }//End of observeTaskList method

    private void updateTaskUI(){
        if (firstTask != null) {
            TextView taskTitle = binding.taskTitleId;
            TextView sessionCount = binding.sessionCountId;

            String sessionCountText = firstTask.getRemainingSessions() + "/" + firstTask.getSessionCount();

            taskTitle.setText(firstTask.getTaskTitle());
            sessionCount.setText(sessionCountText);
            Log.d("LOG_TASK_UI_UPDATE", "Updated UI for task: " + firstTask.getTaskTitle() +
                    " with sessions: " + sessionCountText);
        }
    }//End of updateTaskUI method

    private void listenSession(){
        timerVM.getSessionFinished().observe(getViewLifecycleOwner(), finished -> {
            if (finished != null && finished) {//If session is finished
                if (isUserLoggedIn && timerVM.wasSessionStartedWhileLoggedIn()) {
                    if("Pomodoro".equals(currentType)){
                        timerVM.recordPomodoroSession(userId, timerVM.getTotalTime());
                        timerVM.saveTotalFocus(userId, timerVM.getTotalTime());
                        Log.d("LOG_RECORD_SESSION_AND_FOCUS","FOCUS SAVED");
                        if (firstTask != null) {
                            taskVM.decreaseTaskSession(firstTask);
                            Log.d("LOG_TASK_SESSION_DECREASED", "Task session decreased for: " + firstTask.getTaskTitle());
                        }
                    }else {
                        timerVM.recordBreakSession(userId, timerVM.getTotalTime());
                        timerVM.saveTotalBreakTime(userId, timerVM.getTotalTime());
                        Log.d("LOG_RECORD_SESSION_AND_BREAK","BREAK SAVED");
                    }
                }else {
                    if (firstTask != null) {
                        taskVM.decreaseTaskSession(firstTask);
                        Log.d("LOG_TASK_SESSION_DECREASED", "Task session decreased for: " + firstTask.getTaskTitle());
                    }
                }
                updateTimerTotalTime();
                timerVM.clearSessionFinished();
            }
        });
    }//End of listenSession method

//    private void recordSession(){
//        if (userId == null) {
//            Log.e("LOG_USER_ID_TIMER_FRAGMENT", "User ID is null! Cannot log session.");
//            return;
//        }
//
//        long timestamp = System.currentTimeMillis();
//        int sessionCount = 1; //Each Pomodoro session = 1
//
//        PomodoroLogModel log = new PomodoroLogModel(userId, timestamp, sessionCount);
//        timerVM.insert(log);
//    }//End of recordSession method

}
