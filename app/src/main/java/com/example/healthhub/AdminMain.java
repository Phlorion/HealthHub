package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class AdminMain extends AppCompatActivity {

    Button logoutbtn;
    ImageButton setPersonalInfo; //TODO Na to kano textView kai na travaei info apo afto pou exei valei o admin
    ImageButton setMedication;
    ImageButton setHome;
    ImageButton setFavContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        logoutbtn = (Button) findViewById(R.id.logout_btn);
        setPersonalInfo = (ImageButton) findViewById(R.id.set_Personal_Info_btn);
        setMedication = (ImageButton) findViewById(R.id.set_Medication_btn);
        setHome = (ImageButton) findViewById(R.id.set_Home_btn);
        setFavContact = (ImageButton) findViewById(R.id.set_Fav_Contacts_btn);

        Glide.with(this)
                .load(R.drawable.personal_info)
                .into(setPersonalInfo);

        Glide.with(this)
                .load(R.drawable.medicine)
                .into(setMedication);

        Glide.with(this)
                .load(R.drawable.home)
                .into(setHome);

        Glide.with(this)
                .load(R.drawable.family)
                .into(setFavContact);

        setPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminMain.this,AdminSetPersonalInfo.class);
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                startActivity(intent);
            }
        });


        setHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminMain.this,AdminSetHome.class);
                Intent preIntent = getIntent();
                int passId = preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                startActivity(intent);
            }
        });


        setMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminMain.this,AdminSetMedication.class);
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                startActivity(intent);
            }
        });


        setFavContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);

                Intent intent = new Intent(AdminMain.this, AdminSetFavoriteContact.class);
                intent.putExtra("userId",passId);
                startActivity(intent);
                finish();

            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminMain.this,EnterAdminUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
