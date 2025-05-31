package com.example.healthhub;

import android.content.pm.PackageManager;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.healthhub.AI.AIManager;
import com.example.healthhub.AI.AppActionSpeaker;
import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;

public class UserMain extends AppCompatActivity implements AppActionSpeaker {

    Button logoutbtn;
    ImageButton aiVoiceAssistant, getMeHomebtn,vMedicationbtn,sosbtn,healthFacilitiesbtn;
    TextView vPersonalInfo;
    private AIManager aiManager; // Instance of the AI manager
    private String[] permissions = {android.Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Intent preIntent = getIntent();
        int passId= preIntent.getIntExtra("userId",-1);
        User curUsr = Utils.userDAO.findUserByID(passId);
        String info=curUsr.getPersonalInfo();

        logoutbtn = (Button) findViewById(R.id.logout_btn);
        aiVoiceAssistant = findViewById(R.id.ai_button);
        getMeHomebtn = (ImageButton) findViewById(R.id.get_Me_Home_btn);
        sosbtn = (ImageButton) findViewById(R.id.sos_btn);
        vPersonalInfo = (TextView) findViewById(R.id.view_Personal_Info_btn);
        healthFacilitiesbtn = (ImageButton) findViewById(R.id.health_facilities_btn);
        vMedicationbtn = (ImageButton) findViewById(R.id.view_Medication_btn);

        Glide.with(this)
                .load(R.drawable.home)
                .into(getMeHomebtn);

        Glide.with(this)
                .load(R.drawable.sos)
                .into(sosbtn);

        Glide.with(this)
                .load(R.drawable.pharmacy)
                .into(healthFacilitiesbtn);

        Glide.with(this)
                .load(R.drawable.medicine)
                .into(vMedicationbtn);

        vPersonalInfo.setText(info);

        getMeHomebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                Intent intent = new Intent(UserMain.this,UserGetHome.class);
                intent.putExtra("userId",passId);
                startActivity(intent);
//                finish();
            }
        });

        vMedicationbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                Intent intent = new Intent(UserMain.this,UserMedicationReminder.class);
                intent.putExtra("userId",passId);
                startActivity(intent);
//                finish();
            }
        });


        sosbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                Intent intent = new Intent(UserMain.this,UserSos.class);
                intent.putExtra("userId",passId);
                startActivity(intent);
                finish();
            }
        });

        healthFacilitiesbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                Intent intent = new Intent(UserMain.this, UserGetNearHealthFacilities.class);
                intent.putExtra("userId",passId);
                startActivity(intent);
//                finish();
            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserMain.this,EnterAdminUserActivity.class);
                Utils.clearLoginSession(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });

        aiManager = AIManager.getInstance(getApplicationContext());
        aiVoiceAssistant.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                aiManager.startListening();
            } else {
                showToast("Microphone permission is required to speak.");
                requestAudioPermission(); // Request permission if not granted
            }
        });
        // Initial permission check on creation
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestAudioPermission();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateStatus(String status) {
    }

    private void requestAudioPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            System.out.println("RECORD_AUDIO permission already granted.");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Audio recording permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Audio recording permission denied. Speech recognition may not work.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
