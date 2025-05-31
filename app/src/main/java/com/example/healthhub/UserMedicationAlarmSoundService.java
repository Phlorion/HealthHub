package com.example.healthhub;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

public class UserMedicationAlarmSoundService extends Service {
    private PowerManager.WakeLock wakeLock;
    private final String CHANNEL_ID = "alarm_channel";
    private final int NOTIFICATION_ID = 3;
//    public static final String CHANNEL_ID = "UserMedicationAlarmSoundService";
    public UserMedicationAlarmSoundService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        /*// Create Notification Channel for Foreground Service (required for Android 8.0+)
        createNotificationChannel();

        Intent fullScreenIntent = new Intent(this, UserMedicationAlarmActivity.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_IMMUTABLE
        );
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("HealthHub")
                .setContentText("Medication alarm is ringing")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true);

        startForeground(1, builder.build());

        // Launch activity manually (in case fullScreenIntent doesn't do it reliably)
        startActivity(fullScreenIntent);

        // Stop service after a short delay (optional)
        new Handler(Looper.getMainLooper()).postDelayed(this::stopSelf, 10_000);
*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        createNotificationChannel();
        Notification notification = buildNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        }else{
            startForeground(NOTIFICATION_ID, notification);
        }
        // Launch the alarm activity
        Intent activityIntent = new Intent(this, UserMedicationAlarmActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
        // Optional: Release wake lock after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }
            stopSelf();
        }, 10 * 1000); // 10 seconds

        return START_NOT_STICKY;
//        return START_STICKY; // Service will be restarted if killed by the system
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true); // Remove notification when service stops
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "MyApp:AlarmWakeLock"
        );
        wakeLock.acquire(10 * 60 * 1000L); // max 10 minutes
    }
    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Medication Reminder")
                .setContentText("Time to take your medication!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Used for alarm notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}