package com.example.pomodoro_timer.ui.fragments.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.codersroute.flexiblewidgets.FlexibleSwitch;
import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsThemeBinding;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;

import java.util.Arrays;
import java.util.List;

public class ThemeSettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsThemeBinding binding;
    private SettingsViewModel settingsVM;
    private NavController navController;
    private List<String> themeList;
    private List<String> alarmList;
    private boolean isSpinnerInitialized = false;

    public ThemeSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSettingsThemeBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        navController = NavHostFragment.findNavController(ThemeSettingsFragment.this);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context Here
        initStuff();

        return binding.getRoot();
    }

    private void initStuff(){
        themeList = Arrays.asList("Theme 1", "Theme 2", "Theme 3");
        alarmList = Arrays.asList("Alarm 1", "Alarm 2", "Alarm 3");
        notificationsSwitch();
        themeSpinnerAdapter();
        alarmSpinnerAdapter();
        switchStatusListener();
    }//End of initStuff method

    private void notificationsSwitch(){
        binding.allowNotificationSwitchId.addOnStatusChangedListener(b -> {
            Log.d("LOG_SWITCH", "onStatusChanged: " + b);
            settingsVM.setAllowNotifications(b);
        });
    }//End of notificationsSwitch method

    private void switchStatusListener(){
        settingsVM.getAllowNotifications().observe(getViewLifecycleOwner(), allow -> {
            binding.allowNotificationSwitchId.setChecked(allow);
        });
    }//End of switchStatusListener method

    private void themeSpinnerAdapter(){
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_theme_spinner, themeList);
        themeAdapter.setDropDownViewResource(R.layout.item_theme_spinner_dropdown);
        binding.themeSpinnerId.setAdapter(themeAdapter);

        // Load current theme
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int savedThemeIndex = prefs.getInt("selected_theme_index", 0);
        binding.themeSpinnerId.setSelection(savedThemeIndex);

        binding.themeSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                    return;
                }

                if (position != savedThemeIndex) {
                    prefs.edit().putInt("selected_theme_index", position).apply();
                    settingsVM.setSelectedTheme(themeList.get(position));
                    navController.popBackStack(R.id.menu_timer, false);
                    requireActivity().recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }//End of themeSpinnerAdapter method

    private void alarmSpinnerAdapter() {
        ArrayAdapter<String> alarmAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_theme_spinner, alarmList);
        alarmAdapter.setDropDownViewResource(R.layout.item_theme_spinner_dropdown);
        binding.alarmSpinnerId.setAdapter(alarmAdapter);

        // Set selected item from ViewModel
        settingsVM.getSelectedAlarm().observe(getViewLifecycleOwner(), selected -> {
            int index = alarmList.indexOf(selected);
            if (index >= 0) {
                binding.alarmSpinnerId.setSelection(index);
            }
            //Save selected alarm to a shared preference
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            sharedPreferences.edit().putString("selected_alarm", selected).apply();
        });
        // Listen for user selection
        binding.alarmSpinnerId.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedAlarm = alarmList.get(position);
                settingsVM.setSelectedAlarm(selectedAlarm);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Optional: handle no selection
            }
        });

    }

}
