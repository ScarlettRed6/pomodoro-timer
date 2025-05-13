package com.example.pomodoro_timer.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
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

        return binding.getRoot();
    }//End of onCreateView method

    private void initStuff(){
        barChart = binding.pomodoroCountPerWeekBarGraphId;
        userId = sharedVM.getCurrentUserId().getValue();
        dateFilter = Arrays.asList("Week", "Month", "Year");
        dateSpinnerAdapter();
        handleBarGraph();
    }//End of initStuff method

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

    private void setupBarGraph(List<PomodoroLogModel> logs){
        List<BarEntry> entries = new ArrayList<>();
        Map<String, Integer> dailySessions = new TreeMap<>();

        //Create map for last 7 days with 0 by default
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault()); // "Mon", "Tue", etc.
        List<String> last7Days = new ArrayList<>();

        long now = System.currentTimeMillis();
        long oneDayMillis = 24 * 60 * 60 * 1000;

        for (int i = 6; i >= 0; i--) {
            Date date = new Date(now - i * oneDayMillis);
            String dayName = sdf.format(date);
            last7Days.add(dayName);
            dailySessions.put(dayName, 0); // Fill 0 by default
        }

        //Aggregate logs into the map
        for (PomodoroLogModel log : logs) {
            String dayName = sdf.format(new Date(log.getTimestamp()));
            if (dailySessions.containsKey(dayName)) {
                int current = dailySessions.get(dayName);
                dailySessions.put(dayName, current + log.getSessionCount());
            }
        }

        //This Build entries using correct X index
        for (int i = 0; i < last7Days.size(); i++) {
            String day = last7Days.get(i);
            int count = dailySessions.get(day);
            entries.add(new BarEntry(i, count));
        }

        //The Chart setup
        BarDataSet dataSet = new BarDataSet(entries, "Pomodoro Sessions");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.text_color));
        dataSet.setValueTextSize(12F);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(last7Days));
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

}
