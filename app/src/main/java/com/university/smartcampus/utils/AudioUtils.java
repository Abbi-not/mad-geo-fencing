package com.university.smartcampus.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.app.NotificationManager;
import com.university.smartcampus.R;

public class AudioUtils {

    /**
     * Toggles the device ringer between Mute and Normal.
     */
    public static void setSilentMode(Context context, boolean shouldBeSilent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check for DND policy access (required for Android 6+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                return; // Don't proceed if we don't have permission
            }
        }

        if (shouldBeSilent) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    /**
     * Plays the audio file stored in res/raw/
     */
    public static void playWelcomeSound(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.welcome_home);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            // Release memory when done
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
    }
}