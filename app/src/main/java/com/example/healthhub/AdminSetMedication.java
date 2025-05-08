package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.DAO.Medication;
import com.example.healthhub.Utils.Utils;

import java.util.ArrayList;

public class AdminSetMedication extends AppCompatActivity implements AdminSetMedication_RecyclerViewInterface{
    ArrayList<Medication> medications;
    Button backBtn, saveBtn;
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

        RecyclerView recyclerView = findViewById(R.id.medications_slots_recycler_view);
        medications = Utils.medicationDAO.findMedicationsByUsedID(Utils.getStoredUserId(getApplicationContext()));
        AdminSetMedication_RecyclerViewAdapter adapter = new AdminSetMedication_RecyclerViewAdapter(this,medications,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.back_button);
        saveBtn = findViewById(R.id.save_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedications();
            }
        });
    }

    private void saveMedications(){
        Utils.medicationDAO.saveMedications(medications);
    }
    @Override
    public void onItemClick(int position) {
        System.out.println("item on click");
    }
}