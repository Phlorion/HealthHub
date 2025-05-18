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
    Button backBtn, addBtn;
    RecyclerView recyclerView;
    AdminSetMedication_RecyclerViewAdapter adapter;
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

        recyclerView = findViewById(R.id.medications_slots_recycler_view);
        medications = fetchMedications();
        medications.forEach(System.out::println);//TODO: Remove
        adapter = new AdminSetMedication_RecyclerViewAdapter(this,medications,this);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        medications = fetchMedications();
        adapter = new AdminSetMedication_RecyclerViewAdapter(this,medications,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void gotoAddEdit(){
        Intent intent = new Intent(this, AdminSetMedicationAddEdit.class);
        startActivity(intent);
    }
    private ArrayList<Medication> fetchMedications(){
        return Utils.medicationDAO.findMedicationsByUsedID(Utils.getStoredUserId(getApplicationContext()));
    }
    @Override
    public void onItemClick(int position) {
        System.out.println("item on click");
    }
}