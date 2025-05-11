package com.example.pomodoro_timer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentStatsBinding;
import com.example.pomodoro_timer.viewmodels.StatsViewModel;

import java.util.Arrays;
import java.util.List;

public class StatsFragment extends Fragment {

    //Fields
    private FragmentStatsBinding binding;
    private StatsViewModel statsVM;
    private List<String> dateFilter;

    public StatsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        statsVM = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);
        binding.setStatsVM(statsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();

        return binding.getRoot();
    }//End of onCreateView method

    private void initStuff(){
        dateFilter = Arrays.asList("Week", "Month", "Year");
        dateSpinnerAdapter();
    }//End of initStuff method

    private void dateSpinnerAdapter(){
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_theme_spinner, dateFilter);
        dateAdapter.setDropDownViewResource(R.layout.item_theme_spinner_dropdown);
        binding.dateSpinnerId.setAdapter(dateAdapter);
    }

}
