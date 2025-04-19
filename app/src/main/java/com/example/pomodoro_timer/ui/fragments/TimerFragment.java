package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTimerBinding;
import com.example.pomodoro_timer.ui.custom.TimerAnimationView;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TimerViewModel;

public class TimerFragment extends Fragment {

    //Fields
    private TimerViewModel timerVM;
    private TimerAnimationView timerAnimView;
    private SharedViewModel sharedVM;

    public TimerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        FragmentTimerBinding binding = FragmentTimerBinding.inflate(inflater, container, false);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timerVM = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);
        binding.setTimerVM(timerVM);
        binding.setLifecycleOwner(this);

        timerListener(binding);

        return binding.getRoot();
    }

    private void timerListener(FragmentTimerBinding binding){
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

    @Override
    public void onResume(){
        super.onResume();
        sharedVM.setShowAddTaskBtn(false);
    }

    @Override
    public void onPause(){
        super.onPause();
        sharedVM.setShowAddTaskBtn(true);
    }
}
