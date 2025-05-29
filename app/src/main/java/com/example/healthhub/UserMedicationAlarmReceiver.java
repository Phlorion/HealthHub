package com.example.healthhub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class UserMedicationAlarmReceiver extends BroadcastReceiver {
    private PowerManager.WakeLock wakeLock;
    private final String CHANNEL_ID = "alarm_channel";
    private final int NOTIFICATION_ID = 3;
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            System.out.println("What!!!!!!!!");
            return;
        }
        acquireWakeLock(context);

        Intent serviceIntent = new Intent(context, UserMedicationAlarmSoundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
        /*PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("Wakelock")
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                "MyApp:AlarmWakeLockTag"
        );

        // Acquire wake lock for max 10 minutes (in case something hangs)
        wl.acquire(10 * 60 * 1000L);

        createNotificationChannel(context);
        Intent fullScreenIntent = new Intent(context, UserMedicationAlarmActivity.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your icon
                .setContentTitle("Medication Reminder")
                .setContentText("Time to take your medication!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_m4a))
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (wl.isHeld()) wl.release();
        }, 5000); // Release after 5 seconds*/
    }
    private void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "MyApp:AlarmWakeLock"
        );
        wakeLock.acquire(10 * 60 * 1000L); // max 10 minutes
    }
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_m4a);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setSound(soundUri, audioAttributes);
            channel.enableVibration(true);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    /*private final String CHANNEL_ID = "alarm_channel";
    private final int NOTIFICATION_ID = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;

        // Create a Notification Channel (required for Android 8.0 and above)
        createNotificationChannel(context);

        // Create the FullScreenIntent for the AlarmActivity
        Intent fullScreenIntent = new Intent(context, UserMedicationAlarmActivity.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // PendingIntent for the FullScreenIntent
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app icon
                        .setContentTitle("Time to Act!")
                        .setContentText("Your scheduled alert is here.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setFullScreenIntent(fullScreenPendingIntent, true) // This is the key for full-screen!
                        .setAutoCancel(true); // Dismiss notification when user taps it

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Notifications";
            String description = "Channel for important alarm notifications that trigger full-screen intent";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(null, null); // No sound for the notification itself, as AlarmActivity/Service plays it
            channel.enableLights(true);
            channel.enableVibration(true);

            // Register the channel with the system
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }*/
}