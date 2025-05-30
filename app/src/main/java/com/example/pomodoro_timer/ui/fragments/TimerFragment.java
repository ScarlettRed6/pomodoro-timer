package com.example.pomodoro_timer.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTimerBinding;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.ui.custom.TimerAnimationView;
import com.example.pomodoro_timer.utils.timer_utils.TimerReceiver;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;
import com.example.pomodoro_timer.viewmodels.TimerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class TimerFragment extends Fragment {

    //Fields
    private TimerViewModel timerVM;
    private TimerAnimationView timerAnimView;
    private TaskViewModel taskVM;
    private FragmentTimerBinding binding;
    private SettingsViewModel settingsVM;
    private SharedViewModel sharedVM;
    private TaskModel currentDisplayedTask;
    private ImageView startBtnIcon;
    private BottomNavigationView bottomNav;
    private Integer userId;
    private Integer pMinutes, sMinutes, lMinutes;
    private Integer pSeconds, sSeconds, lSeconds;
    private String currentType = "Pomodoro";
    private boolean isUserLoggedIn = false;
    private boolean isAllowNotifications = false;
    private static final int TIMER_ALARM_REQUEST_CODE = 731;

    public TimerFragment(){
        //Required empty public constructor
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
        listenUserInterval();
        timerListener();
        initializeTimers();
        settingsVM.getPomodoroMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime());
        settingsVM.getPomodoroSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime());
        settingsVM.getShortBreakMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime());
        settingsVM.getShortBreakSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime());
        settingsVM.getLongBreakMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime());
        settingsVM.getLongBreakSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime());

        settingsVM.getAllowNotifications().observe(getViewLifecycleOwner(), allow -> {
            isAllowNotifications = Objects.requireNonNullElse(allow, false);
        });

        listenSession();
    }//End of init method

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();

        sharedVM.getIsUserLoggedIn().observe(getViewLifecycleOwner(), loggedIn -> {
            isUserLoggedIn = loggedIn;
            timerVM.setUserLoggedIn(isUserLoggedIn);
            if (isUserLoggedIn) {
                taskVM.displayTask(userId);
                taskVM.displayCategory(userId);
                observeTaskList();
                Log.d("LOG_USER_LOGGED_IN", "USER LOGGED IN: USER TASKS/CATEGORIES APPLIED");
            }else {
                taskVM.initializeTasks();
                taskVM.initializeCategories();
                taskVM.displayTask(1);
                taskVM.displayCategory(1);
                observeTaskList();
                Log.d("LOG_USER_NOT_LOGGED_IN", "USER NOT LOGGED IN: GUEST TASKS/CATEGORIES APPLIED");
            }
        });

    }//End of checkUser method

    private void listenUserInterval(){
        settingsVM.getDefaultLBIntervalString().observe(getViewLifecycleOwner(), interval -> timerVM.setDefaultLBInterval(Integer.parseInt(interval)));
        Log.d("LOG_DEFAULT_LB_INTERVAL", "DEFAULT LB INTERVAL: " + settingsVM.getDefaultLBIntervalString().getValue());
    }//End of listenUserInterval method

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

    @SuppressLint("ScheduleExactAlarm")
    private void timerListener(){
        timerAnimView = binding.timerAnimationViewId;

        timerVM.getProgressAngle().observe(getViewLifecycleOwner(), angle -> {
            if(angle != null){
                timerAnimView.setProgress(angle);
                //Log.d("ANGLE", "THIS THANG IS BEING CALLED: TIMERFRAGMENT");
            }
        });

        FrameLayout startBtn = binding.startBtn;
        startBtnIcon = binding.startBtnIcon;
        FrameLayout stopBtn = binding.stopBtn;
        ImageView stopBtnIcon = binding.stopBtnIcon;
        if (getActivity() != null) {
            bottomNav = getActivity().findViewById(R.id.bottom_nav);
        }

        binding.startBtnIcon.setImageResource(R.drawable.ic_start);
        startBtn.setOnClickListener(v -> {
            startBtnIcon.animate()
                        .rotation(270)
                        .setDuration(150)
                        .withEndAction(() -> startBtnIcon.animate()
                                .rotation(0)
                                .setDuration(150));
            //End of animation

            if(Boolean.TRUE.equals(timerVM.getIsRunning().getValue())){
                timerVM.pauseTimer();
                cancelSystemAlarm();
                bottomNavSetVisible();
                binding.startBtnIcon.setImageResource(R.drawable.ic_start);
            } else{
                timerVM.startOrResumeTimer();
                scheduleSystemsAlarm();
                bottomNavSetGone();
                binding.startBtnIcon.setImageResource(R.drawable.ic_pause);
            }
        });//End of startBtn listener

        stopBtn.setOnClickListener(v -> {
            bottomNavSetVisible();
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
            cancelSystemAlarm();
            binding.startBtnIcon.setImageResource(R.drawable.ic_start);
        });

    }//End of timerListener method

    private void autoStartPomodoro(){
        boolean autoStartPomodoro = settingsVM.getAutoStartPomodoro().getValue();
        binding.getRoot().postDelayed(() -> {
            if (autoStartPomodoro && !"Pomodoro".equals(timerVM.getTimerTypeText().getValue())) {
                timerVM.startOrResumeTimer();
                Log.d("LOG_AUTOPOMO", "AUTO START POMODORO , CHECK TIMERTYPE: " + currentType);
            }
        }, 100);
    }//End of autoStartPomodoro method

    private void autoStartBreak(){
        boolean autoStartBreaks = settingsVM.getAutoStartBreaks().getValue();
        binding.getRoot().postDelayed(() -> {
            if (autoStartBreaks && !("Short Break".equals(timerVM.getTimerTypeText().getValue()) || "Long Break".equals(timerVM.getTimerTypeText().getValue()))) {
                timerVM.startOrResumeTimer();
                Log.d("LOG_AUTOBREAK", "AUTO START BREAK , CHECK TIMERTYPE: " + currentType);
            }
        }, 100);
    }//End of autoStartBreak method

    private void bottomNavSetGone(){
        //This sets the animation of bottom nav when GONE it has animation of fade out
        bottomNav.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> bottomNav.setVisibility(View.GONE))
                .start();
    }//End of bottomNavSetGone method

    private void bottomNavSetVisible(){
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.animate()
                .alpha(1f)
                .setDuration(300)
                .start();
    }//End of bottomNavSetVisible method

    private void observeTaskList(){
        taskVM.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            if(tasks != null && !tasks.isEmpty()){
                TaskModel newTask = tasks.get(0);
                // Check if task has changed before animating
                if (currentDisplayedTask == null || !Objects.equals(currentDisplayedTask.getId(), newTask.getId())) {
                    binding.firstTaskCardId.animate()
                            .alpha(0f)
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .setDuration(200)
                            .withEndAction(() -> {
                                currentDisplayedTask = newTask;
                                updateTaskUI();

                                binding.firstTaskCardId.setAlpha(0f);
                                binding.firstTaskCardId.setScaleX(0.95f);
                                binding.firstTaskCardId.setScaleY(0.95f);
                                binding.firstTaskCardId.setVisibility(View.VISIBLE);
                                binding.firstTaskCardId.animate()
                                        .alpha(1f)
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(200)
                                        .start();
                            })
                            .start();
                } else {
                    // No change in task, just update UI without animation
                    currentDisplayedTask = newTask;
                    updateTaskUI();
                }
            } else {
                currentDisplayedTask = null;
                binding.firstTaskCardId.setVisibility(View.GONE);
                Log.d("LOG_NO_TASKS", "No incomplete tasks available");
            }
        });
    }//End of observeTaskList method

    private void updateTaskUI(){
        if (currentDisplayedTask != null) {
            TextView taskTitle = binding.taskTitleId;
            TextView sessionCount = binding.sessionCountId;

            String sessionCountText = currentDisplayedTask.getSessionsCompleted() + "/" + currentDisplayedTask.getSessionCount();

            taskTitle.setText(currentDisplayedTask.getTaskTitle());
            sessionCount.setText(sessionCountText);
            Log.d("LOG_TASK_UI_UPDATE", "Updated UI for task: " + currentDisplayedTask.getTaskTitle() +
                    " with sessions: " + sessionCountText);
        }
    }//End of updateTaskUI method

    private PendingIntent getTimerAlarmPendingIntent(boolean create){
        Intent intent = new Intent(requireContext(), TimerReceiver.class);

        int flags = PendingIntent.FLAG_IMMUTABLE;
        if (create){
            flags |= PendingIntent.FLAG_UPDATE_CURRENT;
        }else {
            flags |= PendingIntent.FLAG_UPDATE_CURRENT;
        }
        return PendingIntent.getBroadcast(requireContext(), TIMER_ALARM_REQUEST_CODE, intent, flags);
    }//End of getTimerAlarmPendingIntent method

    private void scheduleSystemsAlarm(){
        if (!isAllowNotifications || Boolean.FALSE.equals(timerVM.getIsRunning().getValue()) || requireContext() == null){
            Log.d("LOG_TIMER_FRAGMENT", "Not scheduling system alarm: notifications disabled, timer not running, or context null.");
            return;
        }

        // Ensure getRemainingTime() doesn't return 0 or negative if the timer just finished internally
        long remainingTime = timerVM.getRemainingTime();
        if (remainingTime <= 0) {
            Log.d("TimerFragment", "Not scheduling system alarm: remaining time is zero or less.");
            return;
        }

        PendingIntent pendingIntent = getTimerAlarmPendingIntent(true); // 'true' to create/update

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long triggerTime = System.currentTimeMillis() + remainingTime;
            try {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                Log.d("TimerFragment", "System alarm scheduled for: " + triggerTime + " (in " + remainingTime + "ms)");
            } catch (SecurityException se) {
                Log.e("TimerFragment", "SecurityException: Failed to schedule exact alarm. Check SCHEDULE_EXACT_ALARM permission.", se);
                // Handle this case, perhaps by informing the user or using a less exact alarm
            }
        }
    }

    private void cancelSystemAlarm() {
        if (requireContext() == null) {
            Log.d("TimerFragment", "Cannot cancel system alarm: context is null.");
            return;
        }
        PendingIntent pendingIntent = getTimerAlarmPendingIntent(false);

        if (pendingIntent == null){
            Log.w("LOG_NULL_PENDING_INTENT", "PENDING INTENT IS NULL when trying to cancel. Alarm might not have been set or context was null.");
            return;
        }

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.d("TimerFragment", "System alarm cancelled.");
        }
    }

//    @SuppressLint("ScheduleExactAlarm")
//    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
//    private void notificationTimer(){
//        if (!isAllowNotifications) {
//            return;
//        }
//        Intent intent = new Intent(requireContext(), TimerReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
//        long triggerTime = System.currentTimeMillis() + timerVM.getRemainingTime();
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
//    }

    private void listenSession(){
        timerVM.getSessionFinished().observe(getViewLifecycleOwner(), finished -> {
            if (finished != null && finished) {//If session is finished
                Log.d("LOG_SESSION_FINISHED", "SESSION FINISHED");
                Log.d("LOG_USERID_TIMERFRAGMENT", "USER ID: " + userId);
                bottomNavSetVisible();
                startBtnIcon.setImageResource(R.drawable.ic_start);
                if (isUserLoggedIn && timerVM.wasSessionStartedWhileLoggedIn()) {
                    if("Pomodoro".equals(currentType)){
                        timerVM.recordPomodoroSession(userId, timerVM.getTotalTime());
                        timerVM.saveTotalFocus(userId, timerVM.getTotalTime());
                        Log.d("LOG_RECORD_SESSION_AND_FOCUS","FOCUS SAVED");
                        if (currentDisplayedTask != null) {
                            taskVM.increaseFinishedTaskSession(currentDisplayedTask);
                            Log.d("LOG_TASK_SESSION_DECREASED", "Task session decreased for: " + currentDisplayedTask.getTaskTitle());
                        }
                    }else {
                        timerVM.recordBreakSession(userId, timerVM.getTotalTime());
                        timerVM.saveTotalBreakTime(userId, timerVM.getTotalTime());
                        Log.d("LOG_RECORD_SESSION_AND_BREAK","BREAK SAVED");
                    }
                }else {
                    if ("Pomodoro".equals(currentType) && currentDisplayedTask != null) {
                        taskVM.increaseFinishedTaskSession(currentDisplayedTask);
                        Log.d("LOG_TASK_SESSION_INCREASED", "Task session increased for guest user: " + currentDisplayedTask.getTaskTitle());
                    }
                }
                updateTimerTotalTime();
                autoStartPomodoro();
                autoStartBreak();
                timerVM.clearSessionFinished();
            }
        });
    }//End of listenSession method

}
