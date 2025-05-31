package com.example.healthhub;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.Adapters.AdminSetMedication_RecyclerViewAdapter;
import com.example.healthhub.Adapters.AdminSetMedication_RecyclerViewInterface;
import com.example.healthhub.DAO.Medication;
import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminSetMedication extends AppCompatActivity implements AdminSetMedication_RecyclerViewInterface {
    ArrayList<Medication> medications;
    Button backBtn, addBtn;
    RecyclerView recyclerView;
    AdminSetMedication_RecyclerViewAdapter adapter;
    User user;
    private static final int ALARM_PERMISSION_REQUEST_CODE = 2;
    // Declare ExecutorService and Handler
    private ExecutorService executorService; // This will handle background tasks
    private Handler mainHandler; // This will post results back to the UI thread
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_set_medication);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        executorService = Executors.newSingleThreadExecutor(); // A single thread for sequential background tasks
        mainHandler = new Handler(Looper.getMainLooper()); // Handler for the main/UI thread


        // get data from previous activity
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            int userId = receivedIntent.getIntExtra("userId", -1);
            user = Utils.userDAO.findUserByID(userId);
        }

        recyclerView = findViewById(R.id.medications_slots_recycler_view);
        medications = new ArrayList<>();
        loadMedicationsAsync();
        adapter = new AdminSetMedication_RecyclerViewAdapter(this,medications,this,user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.back_button);
        addBtn = findViewById(R.id.add_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddEdit();
            }
        });

        checkAndSetAlarmPermission();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        loadMedicationsAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    private void gotoAddEdit(){
        Intent intent = new Intent(this, AdminSetMedicationAddEdit.class);
        intent.putExtra("userId",user.getId());
        startActivity(intent);
    }
    private ArrayList<Medication> fetchMedications(){
        System.out.println("fetchMedications user id: "+user.getId());
        return Utils.medicationDAO.findMedicationsByUsedID(user.getId());
//        return Utils.medicationDAO.findMedicationsByUsedID(Utils.getStoredUserId(getApplicationContext()));
    }
    @Override
    public void onItemClick(int position) {
        System.out.println("item on click");
    }
    private void checkAndSetAlarmPermission() {// For Android 12 (API 31) and above
        System.out.println("checkAndSetAlarmPermission 1");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("checkAndSetAlarmPermission 2");
            ActivityCompat.requestPermissions(
                    this,new String[]{android.Manifest.permission.SCHEDULE_EXACT_ALARM},ALARM_PERMISSION_REQUEST_CODE
            );
            System.out.println("checkAndSetAlarmPermission 3");
        } else {
            System.out.println("checkAndSetAlarmPermission 4");
            setRepeatingAlarm();
        }
        System.out.println("checkAndSetAlarmPermission 5");
    }


    // This callback is for standard runtime permissions (not directly for ACTION_REQUEST_SCHEDULE_EXACT_ALARM system settings launch)
    // but useful if you need to request other permissions in the future.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALARM_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Exact alarm permission granted via dialog (older API or other permissions).", Toast.LENGTH_SHORT).show();
                setRepeatingAlarm();
            } else {
                Toast.makeText(this, "Exact alarm permission denied via dialog (older API or other permissions).", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private void setRepeatingAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, UserMedicationAlarmReceiver.class);

        // FLAG_IMMUTABLE is required for API 31+
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        flags |= PendingIntent.FLAG_IMMUTABLE; // Add IMMUTABLE flag for API 23+


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0, // Request code
                intent,
                flags
        );

        System.out.println("WOWOWOWOWOWOW");
        // Set the alarm to start at 10:00 PM every day (example)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23); // 10 AM
        calendar.set(Calendar.MINUTE, 3);
        calendar.set(Calendar.SECOND, 0);

        // If the set time has already passed for today, set it for tomorrow
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        // Set the alarm. For exact alarms, use setExactAndAllowWhileIdle or setExact.
        // For repeating alarms, setRepeating is also an option.
        // For precise alarms, even in Doze mode
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        // To set a one-time alarm at a specific time:
        // alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMedicationsAsync() {
        System.out.println("loadMedicationsAsync IN");
        executorService.execute(() -> {
            System.out.println("Executing fetchMedications on background thread for user ID: " + user.getId());
            final ArrayList<Medication> fetchedList = Utils.medicationDAO.findMedicationsByUsedID(user.getId());

            mainHandler.post(() -> {
                System.out.println("Updating UI with fetched medications.");
                // Optionally hide the loading spinner here

                // Use the new updateMedications method on the existing adapter instance
                adapter.updateMedications(fetchedList);

                // No need to set adapter or layout manager again here, they were set in onCreate
                System.out.println("loadMedicationsAsync OUT");
            });
        });
    }
}