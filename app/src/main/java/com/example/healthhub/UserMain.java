package com.example.healthhub;

import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;

public class UserMain extends AppCompatActivity {

    Button logoutbtn;
    ImageButton getMeHomebtn;
    TextView vPersonalInfo;
    ImageButton vMedicationbtn;
    ImageButton sosbtn;
    ImageButton healthFacilitiesbtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Intent preIntent = getIntent();
        int passId= preIntent.getIntExtra("userId",-1);
        User curUsr = Utils.userDAO.findUserByID(passId);
        String info=curUsr.getPersonalInfo();

        logoutbtn = (Button) findViewById(R.id.logout_btn);
        getMeHomebtn = (ImageButton) findViewById(R.id.get_Me_Home_btn);
        sosbtn = (ImageButton) findViewById(R.id.sos_btn);
        vPersonalInfo = (TextView) findViewById(R.id.view_Personal_Info_btn);
        healthFacilitiesbtn = (ImageButton) findViewById(R.id.health_facilities_btn);
        vMedicationbtn = (ImageButton) findViewById(R.id.view_Medication_btn);

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
    }
}
