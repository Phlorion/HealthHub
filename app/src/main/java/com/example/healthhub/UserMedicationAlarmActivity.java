package com.example.healthhub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserMedicationAlarmActivity extends AppCompatActivity {
    PowerManager.WakeLock wakeLock = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_medication_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }else{
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            );
        }
        // Acquire a WakeLock to keep the screen on
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "YourApp:AlarmWakeLock"
        );

        // Acquire for a maximum of 10 minutes to prevent battery drain if not dismissed
        wakeLock.acquire(10 * 60 * 1000L);

        // Start the AlarmSoundService to play the sound
        // This is a more robust way to play sound, especially on newer Android versions
        Intent serviceIntent = new Intent(this, UserMedicationAlarmSoundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            // Stop the sound service when OK is pressed
            Intent stopServiceIntent = new Intent(this, UserMedicationAlarmSoundService.class);
            stopService(stopServiceIntent);
            finish(); // Close the activity
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure WakeLock is released
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}