package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        settingsVM.getPomodoroMinutes().observe(getViewLifecycleOwner(), minutes -> {
            updateDisplayTime();
        });
        settingsVM.getPomodoroSeconds().observe(getViewLifecycleOwner(), seconds -> {
            updateDisplayTime();
        });
        handlePomodoroTimePicker();
    }

    private void updateDisplayTime() {
        Integer minutes = settingsVM.getPomodoroMinutes().getValue();
        Integer seconds = settingsVM.getPomodoroSeconds().getValue();

        if (minutes != null && seconds != null) {
            String formatted = String.format("%02d:%02d", minutes, seconds);
            binding.pomodoroTimeDisplayId.setText(formatted);
        }
    }//End of updateDisplayTime method

    private void handlePomodoroTimePicker(){
        binding.pomodoroTimeDisplayId.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pomodoro_dialog_timer_picker, null);
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

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
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

}
