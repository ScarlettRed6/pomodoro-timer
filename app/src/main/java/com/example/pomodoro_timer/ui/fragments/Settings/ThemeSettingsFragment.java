package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsThemeBinding;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;

import java.util.Arrays;
import java.util.List;

public class ThemeSettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsThemeBinding binding;
    private SettingsViewModel settingsVM;
    private List<String> themeList;
    private List<String> alarmList;

    public ThemeSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSettingsThemeBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context Here
        initStuff();

        return binding.getRoot();
    }

    private void initStuff(){
        themeList = Arrays.asList("Theme 1", "Theme 2", "Theme 3");
        alarmList = Arrays.asList("Alarm 1", "Alarm 2", "Alarm 3");
        themeSpinnerAdapter();
        alarmSpinnerAdapter();
    }

    private void themeSpinnerAdapter(){
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_theme_spinner, themeList);
        themeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.themeSpinnerId.setAdapter(themeAdapter);
    }

    private void alarmSpinnerAdapter(){
        ArrayAdapter<String> alarmAdapter = new ArrayAdapter<>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, alarmList);
        alarmAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.alarmSpinnerId.setAdapter(alarmAdapter);
    }

}
