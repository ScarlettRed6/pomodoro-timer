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
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.ui.custom.TimerAnimationView;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;
import com.example.pomodoro_timer.viewmodels.TimerViewModel;

public class TimerFragment extends Fragment {

    //Fields
    private TimerViewModel timerVM;
    private TimerAnimationView timerAnimView;
    private TaskViewModel taskVM;
    private FragmentTimerBinding binding;
    private SettingsViewModel settingsVM;

    public TimerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        timerVM = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        binding.setTimerVM(timerVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Initialize things
        init();

        return binding.getRoot();
    }//End of onCreateView method

    private void init(){
        taskVM.initializeTasks();
        taskVM.initializeCategories();
        setFirstPriorityTask();
        timerListener();
        settingsVM.getPomodoroMinutes().observe(getViewLifecycleOwner(), minutes -> updateTimerTotalTime(settingsVM));
        settingsVM.getPomodoroSeconds().observe(getViewLifecycleOwner(), seconds -> updateTimerTotalTime(settingsVM));
    }//End of init method

    private void updateTimerTotalTime(SettingsViewModel settingsVM) {
        Integer minutes = settingsVM.getPomodoroMinutes().getValue();
        Integer seconds = settingsVM.getPomodoroSeconds().getValue();

        if (minutes != null && seconds != null) {
            long totalTime = (minutes * 60L + seconds) * 1000L;
            timerVM.setTotalTime(totalTime);
        }
    }//End of updateTimerTotalTime method

    private void timerListener(){
        timerAnimView = binding.timerAnimationViewId;

        timerVM.getProgressAngle().observe(getViewLifecycleOwner(), angle -> {
            if(angle != null){
                timerAnimView.setProgress(angle);
                //Log.d("ANGLE", "GOHDAYUM");
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

    private void setFirstPriorityTask(){
        taskVM.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            if(tasks != null && !tasks.isEmpty()){
                TaskModel firstTask = tasks.get(0);

                TextView taskTitle = binding.taskTitleId;
                TextView sessionCount = binding.sessionCountId;

                taskTitle.setText(firstTask.getTitle());
                sessionCount.setText(String.valueOf(firstTask.getSessionCount()));
                binding.firstTaskCardId.setVisibility(View.VISIBLE);
            }

        });
    }//End of setFirstPriorityTask method

}
