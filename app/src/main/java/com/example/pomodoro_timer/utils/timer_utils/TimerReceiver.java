package com.example.pomodoro_timer.utils.timer_utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.utils.AlarmMapper;

public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedAlarm = prefs.getString("selected_alarm", "Alarm 1");

        // Get the corresponding raw resource ID
        int alarmResId = AlarmMapper.getAlarmResId(selectedAlarm);

        //Start alarm
        AlarmPlayer.play(context, alarmResId);

        //Intent to stop the alarm
        Intent stopIntent = new Intent(context, AlarmStopReceiver.class);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                context, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        //Notification with STOP button
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "pomodoro_timer_channel")
                .setSmallIcon(R.drawable.ic_start)
                .setContentTitle("Pomodoro Timer")
                .setContentText("Timer ended. Tap to stop the alarm.")
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "pomodoro_timer_channel", "Pomodoro Timer", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        manager.notify(100, builder.build());
    }
}
