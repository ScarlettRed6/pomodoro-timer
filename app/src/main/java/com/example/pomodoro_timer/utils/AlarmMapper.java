package com.example.pomodoro_timer.utils;

import com.example.pomodoro_timer.R;

public class AlarmMapper {

    public static int getAlarmResId(String alarmName) {
        switch (alarmName) {
            case "Alarm 1":
                return R.raw.digital_alarm;
            case "Alarm 2":
                return R.raw.lofi_alarm;
            case "Alarm 3":
                return R.raw.funny_alarm;
            default:
                return R.raw.digital_alarm;
        }
    }
}
