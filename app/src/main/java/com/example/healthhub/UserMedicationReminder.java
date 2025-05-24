package com.example.healthhub;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.Adapters.UserMedicationReminder_RecyclerViewAdapter;
import com.example.healthhub.DAO.Medication;
import com.example.healthhub.Utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;

public class UserMedicationReminder extends AppCompatActivity {
    ArrayList<Medication> medications;
    ArrayList<String> medicationsNames,medicationsTimes;
    Button backBtn;
    RecyclerView recyclerView;
    TextView currentTime,currentTimeAMPM;
    UserMedicationReminder_RecyclerViewAdapter adapter;
    // For displaying current time
    private final Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_medication_reminder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medications = fetchTodaysMedications();
        if(medications!=null){
            medications.forEach(System.out::println);//TODO: Remove
        }else {
            medications = fetchMedications();
            System.out.println("Problema");
        }
        medicationsNames = getMedicationsNames(medications);
        medicationsTimes = getMedicationsTimes(medications);
        adapter = new UserMedicationReminder_RecyclerViewAdapter(this,medicationsNames,medicationsTimes);
        recyclerView = findViewById(R.id.todays_medications_slots_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        currentTime = findViewById(R.id.current_time);
        currentTimeAMPM = findViewById(R.id.current_time_ap_pm);
        runnable = new Runnable() {
            @Override
            public void run() {
                currentTime.setText(Utils.getCurrentTimeString());
                currentTimeAMPM.setText(Utils.getCurrentTimeAMPM());
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);

    }
    private ArrayList<Medication> fetchMedications(){
        return Utils.medicationDAO.findMedicationsByUsedID(Utils.getStoredUserId(getApplicationContext()));
    }
    private ArrayList<Medication> fetchTodaysMedications(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Utils.medicationDAO.findMedicationsByUserIDAndDate(Utils.getStoredUserId(getApplicationContext()),LocalDate.now());
        }
        return null;
    }
    private ArrayList<String> getMedicationsNames(ArrayList<Medication> medications){
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i <medications.size() ; i++) {
            for (int j = 0; j <medications.get(i).getTime().size() ; j++) {
                names.add(medications.get(i).getName());
            }
        }
        return names;
    }
    private ArrayList<String> getMedicationsTimes(ArrayList<Medication> medications){
        ArrayList<String> times = new ArrayList<>();
        for (int i = 0; i <medications.size() ; i++) {
            times.addAll(medications.get(i).getTime());
        }
        return times;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}