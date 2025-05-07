package com.example.healthhub;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminMain extends AppCompatActivity {


    ImageButton setPersonalInfo;
    ImageButton setMedication;
    ImageButton setHome;
    ImageButton setFavContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        setPersonalInfo = (ImageButton) findViewById(R.id.set_Personal_Info_btn);
        setMedication = (ImageButton) findViewById(R.id.set_Medication_btn);
        setHome = (ImageButton) findViewById(R.id.set_Home_btn);
        setFavContact = (ImageButton) findViewById(R.id.set_Fav_Contacts_btn);

        setPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMain.this,"setPersonal", Toast.LENGTH_SHORT).show();
            }
        });


        setHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMain.this,"setHome", Toast.LENGTH_SHORT).show();
            }
        });


        setMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMain.this,"setMedication", Toast.LENGTH_SHORT).show();
            }
        });


        setFavContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMain.this,"setFavoriteCont", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
