package com.example.pomodoro_timer.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.example.pomodoro_timer.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarHeatMapView extends View {

    //Default dimensions and colors
    private static final int DEFAULT_SQUARE_CORNER_RADIUS_DP = 4;
    private static final int DEFAULT_SQUARE_SIZE_DP = 32;
    private static final int DEFAULT_SQUARE_SPACING_DP = 2;
    private static final int DEFAULT_TEXT_SIZE_SP = 12;
    private static final int DEFAULT_HEADER_TEXT_SIZE_SP = 16;
    private static final int DEFAULT_DAY_NUMBER_SIZE_SP = 14;
    private static final int DEFAULT_EMPTY_COLOR = Color.parseColor("#EEEEEE");
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#333333");
    private static final int DEFAULT_HEADER_BG_COLOR = Color.parseColor("#F0F0F0");
    private static final int DEFAULT_TODAY_INDICATOR_COLOR = Color.parseColor("#FF5722");
    private static final int[] DEFAULT_HEAT_MAP_COLORS = {
            Color.parseColor("#e5d0ff"), //Lowest
            Color.parseColor("#dabcff"), //
            Color.parseColor("#cca3ff"), //
            Color.parseColor("#bf8bff")  //Highest
    };

    // Paint objects
    private Paint squarePaint;
    private Paint textPaint;
    private Paint headerTextPaint;
    private Paint headerBgPaint;
    private Paint dayNumberPaint;
    private Paint todayIndicatorPaint;

    // Dimensions
    private int squareSize;
    private int squareSpacing;
    private int textSize;
    private int headerTextSize;
    private int dayNumberSize;
    private int headerHeight;
    private float cornerRadius;

    // Colors
    private int emptyColor;
    private int textColor;
    private int headerBgColor;
    private int todayIndicatorColor;
    private int[] heatMapColors;

    // Calendar data
    private Calendar displayedMonth;
    private Map<String, Integer> dataPoints;
    private int monthToShow; // 0-11 for Jan-Dec
    private int yearToShow;

    // For touch handling
    private GestureDetectorCompat gestureDetector;
    private OnDateClickListener dateClickListener;

    public CalendarHeatMapView(Context context) {
        super(context);
        init(null);
    }

    public CalendarHeatMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CalendarHeatMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        // Convert dp to pixels
        Context context = getContext();
        float density = getResources().getDisplayMetrics().density;
        squareSize = (int) (DEFAULT_SQUARE_SIZE_DP * density);
        squareSpacing = (int) (DEFAULT_SQUARE_SPACING_DP * density);
        textSize = (int) (DEFAULT_TEXT_SIZE_SP * density);
        headerTextSize = (int) (DEFAULT_HEADER_TEXT_SIZE_SP * density);
        dayNumberSize = (int) (DEFAULT_DAY_NUMBER_SIZE_SP * density);
        headerHeight = (int) (headerTextSize * 2.5f);
        //Initialize corner radius
        cornerRadius = DEFAULT_SQUARE_CORNER_RADIUS_DP * density;

        // Default colors
        emptyColor = resolveThemeColor(context, R.attr.calendarEmptyColor, DEFAULT_EMPTY_COLOR);
        textColor = resolveThemeColor(context, R.attr.calendarTextColor, DEFAULT_TEXT_COLOR);
        headerBgColor = resolveThemeColor(context, R.attr.calendarHeaderBackgroundColor, DEFAULT_HEADER_BG_COLOR);
        todayIndicatorColor = resolveThemeColor(context, R.attr.calendarTodayIndicatorColor, DEFAULT_TODAY_INDICATOR_COLOR);

        heatMapColors = new int[]{
                resolveThemeColor(context, R.attr.calendarHeatmapColorLowest, DEFAULT_HEAT_MAP_COLORS[3]),
                resolveThemeColor(context, R.attr.calendarHeatmapColorLow, DEFAULT_HEAT_MAP_COLORS[2]),
                resolveThemeColor(context, R.attr.calendarHeatmapColorMedium, DEFAULT_HEAT_MAP_COLORS[1]),
                resolveThemeColor(context, R.attr.calendarHeatmapColorHigh, DEFAULT_HEAT_MAP_COLORS[0])
        };

        // Initialize paints
        squarePaint = new Paint();
        squarePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        headerTextPaint = new Paint();
        headerTextPaint.setAntiAlias(true);
        headerTextPaint.setColor(textColor);
        headerTextPaint.setTextSize(headerTextSize);
        headerTextPaint.setTextAlign(Paint.Align.CENTER);
        headerTextPaint.setFakeBoldText(true);

        headerBgPaint = new Paint();
        headerBgPaint.setColor(headerBgColor);
        headerBgPaint.setStyle(Paint.Style.FILL);

        dayNumberPaint = new Paint();
        dayNumberPaint.setAntiAlias(true);
        dayNumberPaint.setColor(textColor);
        dayNumberPaint.setTextSize(dayNumberSize);
        dayNumberPaint.setTextAlign(Paint.Align.CENTER);

        todayIndicatorPaint = new Paint();
        todayIndicatorPaint.setAntiAlias(true);
        todayIndicatorPaint.setColor(todayIndicatorColor);
        todayIndicatorPaint.setStyle(Paint.Style.STROKE);
        todayIndicatorPaint.setStrokeWidth(density * 5);

        // Initialize date and data
        Calendar today = Calendar.getInstance();
        monthToShow = today.get(Calendar.MONTH);
        yearToShow = today.get(Calendar.YEAR);
        displayedMonth = Calendar.getInstance();
        displayedMonth.set(Calendar.DAY_OF_MONTH, 1);
        displayedMonth.set(Calendar.MONTH, monthToShow);
        displayedMonth.set(Calendar.YEAR, yearToShow);
        dataPoints = new HashMap<>();

        // Setup gesture detector
        gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return handleClick(e.getX(), e.getY());
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = resolveSize(calculateDesiredWidth(), widthMeasureSpec);
        int height = resolveSize(calculateDesiredHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int calculateDesiredWidth() {
        // 7 days across + spacing
        return 7 * (squareSize + squareSpacing) + getPaddingLeft() + getPaddingRight();
    }

    private int calculateDesiredHeight() {
        // Calculate rows needed
        int rows = getWeeksInMonth() + 1; // +1 for header row with day names
        return headerHeight + (rows * (squareSize + squareSpacing)) + getPaddingTop() + getPaddingBottom();
    }

    private int getWeeksInMonth() {
        Calendar cal = (Calendar) displayedMonth.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // Get number of days in month
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Calculate number of weeks
        int offset = firstDayOfWeek - Calendar.SUNDAY;
        int totalDays = offset + daysInMonth;
        int weeks = (totalDays + 6) / 7; // Round up to include partial weeks

        return weeks;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get current month's calendar
        Calendar calendar = (Calendar) displayedMonth.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Figure out which day of the week the month starts on
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Get number of days in month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get today's date for highlighting
        Calendar today = Calendar.getInstance();
        boolean isCurrentMonth = today.get(Calendar.MONTH) == monthToShow &&
                today.get(Calendar.YEAR) == yearToShow;
        int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);

        int startX = getPaddingLeft();
        int startY = getPaddingTop() + headerHeight;

        String monthHeader = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                " " + calendar.get(Calendar.YEAR);

        String[] dayLabels = {"S", "M", "T", "W", "T", "F", "S"};
        for (int i = 0; i < 7; i++) {
            float x = startX + i * (squareSize + squareSpacing) + squareSize / 2f;
            float y = startY - squareSpacing;
            canvas.drawText(dayLabels[i], x, y, textPaint);
        }

        int dayOffset = firstDayOfWeek - Calendar.SUNDAY;

        //Drawing the calendar squares
        int day = 1;
        for (int week = 0; week < getWeeksInMonth(); week++) {
            for (int weekday = 0; weekday < 7; weekday++) {
                int position = week * 7 + weekday;

                //This Skip days before the start of the month
                if (position < dayOffset || day > daysInMonth) {
                    continue;
                }

                Calendar currentDate = (Calendar) calendar.clone();
                currentDate.set(Calendar.DAY_OF_MONTH, day);

                String dateKey = String.format(Locale.US, "%d-%02d-%02d",
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH) + 1,
                        currentDate.get(Calendar.DAY_OF_MONTH));

                //Determine color based on data
                Integer value = dataPoints.get(dateKey);
                int color;

                if (value == null) {
                    color = emptyColor;
                } else {
                    // Map data value to color index
                    int colorIndex = Math.min(value, heatMapColors.length - 1);
                    color = heatMapColors[colorIndex];
                }

                squarePaint.setColor(color);

                // Draw the square
                float left = startX + weekday * (squareSize + squareSpacing);
                float top = startY + week * (squareSize + squareSpacing);
                float right = left + squareSize;
                float bottom = top + squareSize;

                RectF rect = new RectF(left, top, right, bottom);
                // canvas.drawRect(rect, squarePaint); // OLD WAY
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, squarePaint); // NEW WAY

                // Draw day number
                float textX = left + (squareSize / 2f);
                float textY = top + (squareSize / 2f) + (dayNumberSize / 3f); // Approximate vertical centering
                canvas.drawText(String.valueOf(day), textX, textY, dayNumberPaint);

                // Highlight today
                if (isCurrentMonth && day == todayDayOfMonth) {
                    float padding = squareSize * 0.1f;
                    RectF todayRect = new RectF(
                            left + padding,
                            top + padding,
                            right - padding,
                            bottom - padding);
                    canvas.drawRoundRect(todayRect, cornerRadius - padding, cornerRadius - padding, todayIndicatorPaint);
                }

                day++;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private boolean handleClick(float x, float y) {
        if (dateClickListener == null) {
            return false;
        }

        // Skip if clicked in header area
        if (y < getPaddingTop() + headerHeight) {
            return false;
        }

        // Calculate day offset based on first day of month
        Calendar calendar = (Calendar) displayedMonth.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOffset = firstDayOfWeek - Calendar.SUNDAY;

        // Calculate which row and column was clicked
        int row = (int) ((y - getPaddingTop() - headerHeight) / (squareSize + squareSpacing));
        int col = (int) ((x - getPaddingLeft()) / (squareSize + squareSpacing));

        // Convert to day of month
        int dayPosition = row * 7 + col;
        int dayOfMonth = dayPosition - dayOffset + 1;

        // Check if valid day in month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (dayOfMonth < 1 || dayOfMonth > daysInMonth) {
            return false;
        }

        // Set the clicked date
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // Get value for this date
        String dateKey = String.format(Locale.US, "%d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));

        Integer value = dataPoints.get(dateKey);
        if (value == null) {
            value = 0;
        }

        dateClickListener.onDateClick(calendar, value);
        return true;
    }

    // Interface for click callbacks
    public interface OnDateClickListener {
        void onDateClick(Calendar date, int value);
    }

    // Set listener for date clicks
    public void setOnDateClickListener(OnDateClickListener listener) {
        this.dateClickListener = listener;
    }

    // Public method to set data
    public void setDataPoints(Map<String, Integer> data) {
        dataPoints.clear();
        if (data != null) {
            dataPoints.putAll(data);
        }
        invalidate();
    }

    // Add a single data point
    public void addDataPoint(Calendar date, int value) {
        String dateKey = String.format(Locale.US, "%d-%02d-%02d",
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.DAY_OF_MONTH));
        dataPoints.put(dateKey, value);
        invalidate();
    }

    // Set the month to display
    public void setMonth(int month, int year) {
        this.monthToShow = month;
        this.yearToShow = year;

        displayedMonth.set(Calendar.DAY_OF_MONTH, 1);
        displayedMonth.set(Calendar.MONTH, month);
        displayedMonth.set(Calendar.YEAR, year);

        requestLayout();
        invalidate();
    }

    // Move to next month
    public void nextMonth() {
        Calendar cal = (Calendar) displayedMonth.clone();
        cal.add(Calendar.MONTH, 1);
        setMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    // Move to previous month
    public void previousMonth() {
        Calendar cal = (Calendar) displayedMonth.clone();
        cal.add(Calendar.MONTH, -1);
        setMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    // Getters for current month/year
    public int getCurrentMonth() {
        return monthToShow;
    }

    public int getCurrentYear() {
        return yearToShow;
    }

    // Customization methods
    public void setSquareSize(int dp) {
        float density = getResources().getDisplayMetrics().density;
        this.squareSize = (int) (dp * density);
        requestLayout();
        invalidate();
    }

    public void setSquareSpacing(int dp) {
        float density = getResources().getDisplayMetrics().density;
        this.squareSpacing = (int) (dp * density);
        requestLayout();
        invalidate();
    }

    public void setEmptyColor(int color) {
        this.emptyColor = color;
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        textPaint.setColor(color);
        dayNumberPaint.setColor(color);
        headerTextPaint.setColor(color);
        invalidate();
    }

    public void setHeaderBgColor(int color) {
        this.headerBgColor = color;
        headerBgPaint.setColor(color);
        invalidate();
    }

    public void setTodayIndicatorColor(int color) {
        this.todayIndicatorColor = color;
        todayIndicatorPaint.setColor(color);
        invalidate();
    }

    public void setHeatMapColors(int[] colors) {
        this.heatMapColors = colors;
        invalidate();
    }

    private int resolveThemeColor(Context context, int attrRes, int defaultColor) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attrRes, typedValue, true) &&
                typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
                typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return typedValue.data;
        }
        // Log fallback or handle error - good practice to know if a theme attribute is missing
        // You can get the resource name like this:
        // String resourceName = "";
        // try { resourceName = getResources().getResourceName(attrRes); } catch (Exception e) {}
        // Log.w("CalendarHeatMapView", "Theme attribute not found or not a color: " + resourceName + ". Using default.");
        return defaultColor;
    }

}
