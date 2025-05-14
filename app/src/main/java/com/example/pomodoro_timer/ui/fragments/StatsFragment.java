package com.example.pomodoro_timer.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentStatsBinding;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.StatsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class StatsFragment extends Fragment {

    //Fields
    private FragmentStatsBinding binding;
    private SharedViewModel sharedVM;
    private StatsViewModel statsVM;
    private List<String> dateFilter;
    private BarChart barChart;
    private int userId;

    public StatsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        statsVM = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setStatsVM(statsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();
        Log.d("Log_user_id", "User ID: " + sharedVM.getCurrentUserId().getValue() + "Username: " + sharedVM.getCurrentUsername().getValue());

        return binding.getRoot();
    }//End of onCreateView method

    private void initStuff(){
        barChart = binding.pomodoroCountPerWeekBarGraphId;
        listenSessionUserId();
        dateFilter = Arrays.asList("Week", "Month", "Year");
        dateSpinnerAdapter();
        checkUserLoggedIn();
    }//End of initStuff method

    private void checkUserLoggedIn(){
        boolean isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
        if (isUserLoggedIn){
            statsVM.resetStats();
            userId = sharedVM.getCurrentUserId().getValue();
            handleBarGraph();
            updateFirstStats();
        } else {
            barChart.clear();
            statsVM.resetStats();
        }
    }//End of checkUserLoggedIn method

    private void dateSpinnerAdapter(){
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_theme_spinner, dateFilter);
        dateAdapter.setDropDownViewResource(R.layout.item_theme_spinner_dropdown);
        binding.dateSpinnerId.setAdapter(dateAdapter);
    }

    private void handleBarGraph(){
        long now = System.currentTimeMillis();
        long oneWeekAgo = now - (7 * 24 * 60 * 60 * 1000);

        boolean isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();

        if (!isUserLoggedIn){
            barChart.clear();
            return;
        }

        statsVM.getPomodoroLogs(userId, oneWeekAgo, now).observe(getViewLifecycleOwner(), pomodoroLogs -> {
            if(pomodoroLogs != null){
                setupBarGraph(pomodoroLogs);
            }
        });
    }//End of handleBarGraph method

    private void listenSessionUserId(){
        sharedVM.getCurrentUserId().observe(getViewLifecycleOwner(), id -> {
            if (this.userId != id) {  // Only update if ID has changed
                this.userId = id;
                // Important: Force reload stats when user ID changes
                if (sharedVM.getIsUserLoggedIn().getValue()) {
                    handleBarGraph();
                    updateFirstStats();
                }
            }
        });
    }

    private void setupBarGraph(List<PomodoroLogModel> logs){
        Log.d("DATE TODAYS", "DATE: " + System.currentTimeMillis());
        List<BarEntry> entries = new ArrayList<>();

        SimpleDateFormat keyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat labelFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        List<String> labels = new ArrayList<>();
        Map<String, Integer> dailySessions = new TreeMap<>();

        long now = System.currentTimeMillis();
        long oneDayMillis = 24 * 60 * 60 * 1000;

        for (int i = 6; i >= 0; i--) {
            Date date = new Date(now - i * oneDayMillis);
            String key = keyFormat.format(date);     // Unique key
            String label = labelFormat.format(date); // "Mon", "Tue", etc.
            dailySessions.put(key, 0); // Initialize with 0
            labels.add(label);         // Add label for X-axis
        }

        //Aggregate logs into the map
        for (PomodoroLogModel log : logs) {
            String key = keyFormat.format(new Date(log.getTimestamp()));
            if (dailySessions.containsKey(key)) {
                int current = dailySessions.get(key);
                dailySessions.put(key, current + log.getSessionCount());
            }
        }

        //This Build entries using correct X index
        int index = 0;
        for (String key : dailySessions.keySet()) {
            int count = dailySessions.get(key);
            entries.add(new BarEntry(index, count));
            index++;
        }

        //The Chart setup
        BarDataSet dataSet = new BarDataSet(entries, "Pomodoro Sessions");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.text_color));
        dataSet.setValueTextSize(12F);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();

        //Other styles for the bar graph
        dataSet.setGradientColor(Color.parseColor("#7356F3"), Color.parseColor("#F790FA"));

    }//End of setupBarGraph method

    private void updateFirstStats(){
        statsVM.updateStats(userId);
    }

}
