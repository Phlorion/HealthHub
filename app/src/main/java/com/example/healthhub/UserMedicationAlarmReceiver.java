package com.example.healthhub;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class UserMedicationAlarmReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "alarm_channel";
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
    }
}