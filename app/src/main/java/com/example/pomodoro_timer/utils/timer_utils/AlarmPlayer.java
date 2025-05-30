package com.example.pomodoro_timer.utils.timer_utils;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmPlayer {
    private static MediaPlayer mediaPlayer;

    public static void play(Context context, int soundResId) {
        stop();
        mediaPlayer = MediaPlayer.create(context, soundResId);
        if (mediaPlayer != null) {
            // Remove looping so it plays only once
            mediaPlayer.setLooping(false);

            // Set completion listener to auto-stop and clean up
            mediaPlayer.setOnCompletionListener(mp -> {
                stop();
                // Also dismiss the notification when sound finishes
                dismissNotification(context);
            });

            mediaPlayer.start();
        }
    }//End of play method

    public static void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }//End of stop method

    private static void dismissNotification(Context context) {
        android.app.NotificationManager manager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(100); // Same notification ID used in TimerReceiver
        }
    }//End of dismissNotification method

}