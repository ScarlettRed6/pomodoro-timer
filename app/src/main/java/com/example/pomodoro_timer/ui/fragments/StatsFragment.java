package com.example.pomodoro_timer.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentStatsBinding;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.ui.custom.CalendarHeatMapView;
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
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private CalendarHeatMapView calendarHeatMapView;
    private Button prevMonthButton;
    private Button nextMonthButton;
    private MaterialTextView monthYearLabel;
    private BarChart barChart;
    private int userId;

    public StatsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        statsVM = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        calendarHeatMapView = binding.monthlyCalendarView;
        binding.setStatsVM(statsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();
        Log.d("Log_user_id", "User ID: " + sharedVM.getCurrentUserId().getValue() + "Username: " + sharedVM.getCurrentEmail().getValue());

        return binding.getRoot();
    }//End of onCreateView method

    private void initStuff(){
        barChart = binding.pomodoroCountPerWeekBarGraphId;
        dateFilter = Arrays.asList("All","Week", "Month", "Year");
        dateSpinnerAdapter();
        checkUserLoggedIn();
        initCalendarNavigation();

        statsVM.getHeatMapData(userId).observe(getViewLifecycleOwner(), map -> {
            calendarHeatMapView.setDataPoints(map);
        });

        calendarHeatMapView.setOnDateClickListener((date, value) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            String formattedDate = sdf.format(date.getTime());

            String intensity;
            if (value == 0) {
                intensity = "No tasks completed";
            } else if (value == 1) {
                intensity = "1 task completed";
            } else {
                intensity = value + " tasks completed";
            }

            String message = formattedDate + ": " + intensity;
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

    }//End of initStuff method

    // Add this method in initStuff() after your existing initialization
    private void initCalendarNavigation() {
        prevMonthButton = binding.prevMonthButtonId;
        nextMonthButton = binding.nextMonthButtonId;
        monthYearLabel = binding.monthYearLabelId;

        // Set up navigation button listeners
        prevMonthButton.setOnClickListener(v -> {
            calendarHeatMapView.previousMonth();
            updateMonthYearLabel();
            refreshHeatMapData(); // Refresh data for new month
        });

        nextMonthButton.setOnClickListener(v -> {
            calendarHeatMapView.nextMonth();
            updateMonthYearLabel();
            refreshHeatMapData(); // Refresh data for new month
        });

        // Initial label update
        updateMonthYearLabel();
    }

    // Add this method to update the month/year display
    private void updateMonthYearLabel() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, calendarHeatMapView.getCurrentMonth());
        cal.set(Calendar.YEAR, calendarHeatMapView.getCurrentYear());

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearLabel.setText(sdf.format(cal.getTime()));
    }

    // Add this method to refresh heat map data when month changes
    private void refreshHeatMapData() {
        if (userId > 0) {
            statsVM.getHeatMapData(userId).observe(getViewLifecycleOwner(), map -> {
                calendarHeatMapView.setDataPoints(map);
            });
        }
    }

    private void checkUserLoggedIn(){
        boolean isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
        if (isUserLoggedIn){
            statsVM.resetStats();
            userId = sharedVM.getCurrentUserId().getValue();
            listenSessionUserId();
            handleBarGraph("All");
            updateFirstStats();
            Log.d("LOG_CHECK_USER_LOGGED_IN", "User logged in: " + isUserLoggedIn);
        } else {
            barChart.clear();
            statsVM.resetStats();
            Log.d("LOG_CHECK_USER_LOGGED_IN", "User not logged in");
        }
    }//End of checkUserLoggedIn method

    private void dateSpinnerAdapter(){
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_theme_spinner, dateFilter);
        dateAdapter.setDropDownViewResource(R.layout.item_theme_spinner_dropdown);
        binding.dateSpinnerId.setAdapter(dateAdapter);

        binding.dateSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = dateFilter.get(position);
                handleBarGraph(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void handleBarGraph(String filterType){
        long now = System.currentTimeMillis();
        long start = now;

        switch (filterType) {
            case "Week":
                start = now - (7L * 24 * 60 * 60 * 1000); //7 days
                break;
            case "Month":
                start = now - (30L * 24 * 60 * 60 * 1000); //30 days
                break;
            case "Year":
                start = now - (365L * 24 * 60 * 60 * 1000); //1 year
                break;
            case "All":
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                start = calendar.getTimeInMillis();
                break;
        }

        statsVM.getPomodoroLogs(userId, start, now).observe(getViewLifecycleOwner(), pomodoroLogs -> {
            if (pomodoroLogs != null) {
                setupBarGraph(pomodoroLogs, filterType);
            }
        });
    }//End of handleBarGraph method

    private void listenSessionUserId(){
        sharedVM.getCurrentUserId().observe(getViewLifecycleOwner(), id -> {
            if (this.userId != id) {  //Only update if ID has changed
                this.userId = id;
                //Important: Force reload stats when user ID changes
                if (sharedVM.getIsUserLoggedIn().getValue()) {
                    handleBarGraph("All");
                    updateFirstStats();
                }
            }
        });
    }

    private void setupBarGraph(List<PomodoroLogModel> logs, String filterType) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Map<String, Integer> groupedSessions = new TreeMap<>();

        SimpleDateFormat keyFormat = null;
        SimpleDateFormat labelFormat = null;
        long now = System.currentTimeMillis();

        long unitMillis = 1L * 24 * 60 * 60 * 1000;
        int count;
        float barWidth = 0.5f;

        switch (filterType) {
            case "Year":
                unitMillis = 30L * 24 * 60 * 60 * 1000;
                count = 12;
                keyFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                labelFormat = new SimpleDateFormat("MMM", Locale.getDefault());
                barWidth = 0.5f;
                break;
            case "Month":
                unitMillis = 1L * 24 * 60 * 60 * 1000;
                count = 30;
                keyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                labelFormat = new SimpleDateFormat("dd", Locale.getDefault());
                barWidth = 0.4f;
                break;
            case "All":
                unitMillis = 3L * 30 * 24 * 60 * 60 * 1000;
                count = 4;
                barWidth = 0.6f;

                //Initialize quarters
                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                for (int quarter = 1; quarter <= 4; quarter++) {
                    String key = currentYear + "-Q" + quarter;
                    String label = "Q" + quarter;
                    groupedSessions.put(key, 0);
                    labels.add(label);
                }

                //Map each log to its quarter
                for (PomodoroLogModel log : logs) {
                    Calendar logCal = Calendar.getInstance();
                    logCal.setTimeInMillis(log.getTimestamp());
                    int logYear = logCal.get(Calendar.YEAR);
                    int quarter = (logCal.get(Calendar.MONTH) / 3) + 1;

                    if (logYear == currentYear) {
                        String key = currentYear + "-Q" + quarter;
                        groupedSessions.put(key, groupedSessions.getOrDefault(key, 0) + log.getSessionCount());
                    }
                }

                int index = 0;
                for (String key : groupedSessions.keySet()) {
                    entries.add(new BarEntry(index++, groupedSessions.get(key)));
                }
                break;

            case "Week":
            default:
                unitMillis = 1L * 24 * 60 * 60 * 1000;
                count = 7;
                keyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                labelFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                barWidth = 0.7f;
                break;
        }

        if (!filterType.equals("All")) {
            for (int i = count - 1; i >= 0; i--) {
                long time = now - (i * unitMillis);
                String key = keyFormat.format(new Date(time));
                String label = labelFormat.format(new Date(time));
                groupedSessions.put(key, 0);
                labels.add(label);
            }

            for (PomodoroLogModel log : logs) {
                String key = keyFormat.format(new Date(log.getTimestamp()));
                if (groupedSessions.containsKey(key)) {
                    groupedSessions.put(key, groupedSessions.get(key) + log.getSessionCount());
                }
            }

            int index = 0;
            for (String key : groupedSessions.keySet()) {
                entries.add(new BarEntry(index++, groupedSessions.get(key)));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Pomodoro Sessions");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.text_color));
        dataSet.setValueTextSize(12F);
        dataSet.setGradientColor(Color.parseColor("#7356F3"), Color.parseColor("#F790FA"));

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(barWidth);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(filterType.equals("Month") ? -45f : 0f);

        //Add more space between labels and axis for better readability
        xAxis.setYOffset(10f);

        //Set text size based on filter type for better readability
        float textSize = 10f;
        switch (filterType) {
            case "Month":
                textSize = 8f;
                break;
            case "Year":
                textSize = 9f;
                break;
        }
        xAxis.setTextSize(textSize);

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
        barChart.setExtraBottomOffset(10f);
        barChart.setVisibleXRangeMaximum(count > 15 ? 15 : count);
        barChart.setDragEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setScaleEnabled(true);

        barChart.animateY(500);

        if (!entries.isEmpty()) {
            int firstNonZeroIndex = 0;
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getY() > 0) {
                    firstNonZeroIndex = i;
                    break;
                }
            }

            //If there are bars with data, scroll to that position
            if (firstNonZeroIndex > 0) {
                int scrollToIndex = Math.max(0, firstNonZeroIndex - 1);
                barChart.moveViewToX(scrollToIndex);
            }
        }

        barChart.invalidate();
    }//End of setupBarGraph method

    private void updateFirstStats(){
        statsVM.updateStats(userId);
    }
}