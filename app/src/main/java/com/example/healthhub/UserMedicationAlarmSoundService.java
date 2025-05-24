package com.example.healthhub;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class UserMedicationAlarmSoundService extends Service {
    private MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "UserMedicationAlarmSoundService";
    public UserMedicationAlarmSoundService() {
    }
    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        // Create Notification Channel for Foreground Service (required for Android 8.0+)
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm Playing")
                .setContentText("Your scheduled alert is ringing.")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(1, notification); // Start as a foreground service

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_m4a); // Put your sound file in res/raw/
        mediaPlayer.setLooping(true); // Loop the sound
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY; // Service will be restarted if killed by the system
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true); // Remove notification when service stops
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Sound Channel";
            String description = "Channel for foreground alarm sound playback";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Optionally, configure sound for this channel if you want it to be played via notification
            // For this specific use case, we play sound directly via MediaPlayer.
            channel.setSound(null, null); // No sound for the channel itself, as we control it

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}