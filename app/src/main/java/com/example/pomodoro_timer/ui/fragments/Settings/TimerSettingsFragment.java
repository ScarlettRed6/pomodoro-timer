package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsTimerBinding;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;

public class TimerSettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsTimerBinding binding;
    private SettingsViewModel settingsVM;

    public TimerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsTimerBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();

        return binding.getRoot();
    }//End of onCreateView method

    private void initStuff(){
        initializeDisplayTimers();
        handlePomodoroTimePicker();
        handleShortBreakTimePicker();
        handleLongBreakTimePicker();
        autoStartTimersSwitch();
        switchStatusListener();
    }//End of initStuff method

    private void initializeDisplayTimers(){
        settingsVM.getPomodoroMinutes().observe(getViewLifecycleOwner(), minutes -> {
            updateDisplayTime();
        });
        settingsVM.getPomodoroSeconds().observe(getViewLifecycleOwner(), seconds -> {
            updateDisplayTime();
        });
        settingsVM.getShortBreakMinutes().observe(getViewLifecycleOwner(), minutes -> {
            updateDisplayTime();
        });
        settingsVM.getShortBreakSeconds().observe(getViewLifecycleOwner(), seconds -> {
            updateDisplayTime();

            });
        settingsVM.getLongBreakMinutes().observe(getViewLifecycleOwner(), minutes -> {
            updateDisplayTime();
        });
        settingsVM.getLongBreakSeconds().observe(getViewLifecycleOwner(), seconds -> {
            updateDisplayTime();
        });
    }//End of initializeDisplayTimers method

    private void updateDisplayTime() {
        //Get values from view model
        Integer pomodoroMinutes = settingsVM.getPomodoroMinutes().getValue();
        Integer pomodoroSeconds = settingsVM.getPomodoroSeconds().getValue();
        Integer shortBreakMinutes = settingsVM.getShortBreakMinutes().getValue();
        Integer shortBreakSeconds = settingsVM.getShortBreakSeconds().getValue();
        Integer longBreakMinutes = settingsVM.getLongBreakMinutes().getValue();
        Integer longBreakSeconds = settingsVM.getLongBreakSeconds().getValue();

        //Update display
        if (pomodoroMinutes != null && pomodoroSeconds != null) {
            String formatted = String.format("%02d:%02d", pomodoroMinutes, pomodoroSeconds);
            binding.pomodoroTimeDisplayId.setText(formatted);
        }
        if (shortBreakMinutes != null && shortBreakSeconds != null) {
            String formatted = String.format("%02d:%02d", shortBreakMinutes, shortBreakSeconds);
            binding.shortBreakTimeDisplayId.setText(formatted);
        }
        if (longBreakMinutes != null && longBreakSeconds != null) {
            String formatted = String.format("%02d:%02d", longBreakMinutes, longBreakSeconds);
            binding.longBreakTimeDisplayId.setText(formatted);
        }
    }//End of updateDisplayTime method

    private void autoStartTimersSwitch(){
        binding.autoStartPomodoroSwitchId.addOnStatusChangedListener(b -> {
            settingsVM.setAutoStartPomodoro(b);
            Log.d("AUTO START POMODORO", "Auto start pomodoro: " + b);
        });
        binding.autoStartBreaksSwitchId.addOnStatusChangedListener(b -> {
            settingsVM.setAutoStartBreaks(b);
            Log.d("AUTO START BREAKS", "Auto start breaks: " + b);
        });
    }//End of autoStartTimersSwitch method

    private void switchStatusListener(){
        settingsVM.getAutoStartPomodoro().observe(getViewLifecycleOwner(), autoStart -> {
            binding.autoStartPomodoroSwitchId.setChecked(autoStart);
        });
        settingsVM.getAutoStartBreaks().observe(getViewLifecycleOwner(), autoStart -> {
            binding.autoStartBreaksSwitchId.setChecked(autoStart);
        });
    }//End of switchStatusListener method

    private void handlePomodoroTimePicker(){
        binding.pomodoroTimeDisplayId.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pomodoro_timer_picker, null);
            NumberPicker minutePicker = dialogView.findViewById(R.id.pomodoro_minute_picker_id);
            NumberPicker secondPicker = dialogView.findViewById(R.id.pomodoro_second_picker_id);
            //Pomodoro minutes picker
            minutePicker.setMinValue(1);
            minutePicker.setMaxValue(59);
            //Pomodoro seconds picker
            secondPicker.setMinValue(0);
            secondPicker.setMaxValue(59);
            //Set default(Pomodoro technique) value from view model
            int pomodoroMinutes = settingsVM.getPomodoroMinutes().getValue();
            int pomodoroSeconds = settingsVM.getPomodoroSeconds().getValue();
            minutePicker.setValue(pomodoroMinutes);
            secondPicker.setValue(pomodoroSeconds);

            new androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
                    .setTitle("Set Pomodoro Timer")
                    .setView(dialogView)
                    .setPositiveButton("OK", (dialog, which) -> {
                        int newPomodoroMinutes = minutePicker.getValue();
                        int newPomodoroSeconds = secondPicker.getValue();
                        settingsVM.setPomodoroMinutes(newPomodoroMinutes);
                        settingsVM.setPomodoroSeconds(newPomodoroSeconds);
                        updateDisplayTime();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });//End of pomodoro time picker listener
    }//End of handlePomodoroTimePicker method

    private void handleShortBreakTimePicker(){
        binding.shortBreakTimeDisplayId.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_short_break_timer_picker, null);
            NumberPicker minutePicker = dialogView.findViewById(R.id.short_break_minute_picker_id);
            NumberPicker secondPicker = dialogView.findViewById(R.id.short_break_second_picker_id);
            //Short break minutes picker
            minutePicker.setMinValue(1);
            minutePicker.setMaxValue(59);
            //Short break seconds picker
            secondPicker.setMinValue(0);
            secondPicker.setMaxValue(59);
            //Set default(Pomodoro technique) value from view model
            int shortBreakMinutes = settingsVM.getShortBreakMinutes().getValue();
            int shortBreakSeconds = settingsVM.getShortBreakSeconds().getValue();
            minutePicker.setValue(shortBreakMinutes);
            secondPicker.setValue(shortBreakSeconds);
             new androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
                    .setTitle("Set Short Break Timer")
                    .setView(dialogView)
                    .setPositiveButton("OK", (dialog, which) -> {
                        int newShortBreakMinutes = minutePicker.getValue();
                        int newShortBreakSeconds = secondPicker.getValue();
                        settingsVM.setShortBreakMinutes(newShortBreakMinutes);
                        settingsVM.setShortBreakSeconds(newShortBreakSeconds);
                        updateDisplayTime();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });//End of short break time picker listener
    }//End of handleShortBreakTimePicker method

    private void handleLongBreakTimePicker(){
        binding.longBreakTimeDisplayId.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_long_break_timer_picker, null);
            NumberPicker minutePicker = dialogView.findViewById(R.id.long_break_minute_picker_id);
            NumberPicker secondPicker = dialogView.findViewById(R.id.long_break_second_picker_id);
            //Long break minutes picker
            minutePicker.setMinValue(1);
            minutePicker.setMaxValue(59);
            //Long break seconds picker
            secondPicker.setMinValue(0);
            secondPicker.setMaxValue(59);
            //Set default(Pomodoro technique) value from view model
            int longBreakMinutes = settingsVM.getLongBreakMinutes().getValue();
            int longBreakSeconds = settingsVM.getLongBreakSeconds().getValue();
            minutePicker.setValue(longBreakMinutes);
            secondPicker.setValue(longBreakSeconds);
            new androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
                    .setTitle("Set Long Break Timer")
                    .setView(dialogView)
                    .setPositiveButton("OK", (dialog, which) -> {
                        int newLongBreakMinutes = minutePicker.getValue();
                        int newLongBreakSeconds = secondPicker.getValue();
                        settingsVM.setLongBreakMinutes(newLongBreakMinutes);
                        settingsVM.setLongBreakSeconds(newLongBreakSeconds);
                        updateDisplayTime();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        });//End of long break time picker listener
    }//End of handleLongBreakTimePicker method

}
