package com.example.pomodoro_timer.utils.timer_utils;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmPlayer {
    private static MediaPlayer mediaPlayer;

    public static void play(Context context, int soundResId) {
        stop();
        mediaPlayer = MediaPlayer.create(context, soundResId);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
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

}
