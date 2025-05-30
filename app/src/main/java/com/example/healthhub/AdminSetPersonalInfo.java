package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.DAO.User;
import com.example.healthhub.DAO.UserDAO;
import com.example.healthhub.Utils.Utils;

public class AdminSetPersonalInfo extends AppCompatActivity {

    private EditText personalInfoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_personal_info);

        personalInfoEditText = findViewById(R.id.personalInfoEditText);
        Button saveInfoButton = findViewById(R.id.saveInfoButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button goBack = findViewById(R.id.admin_setpersonal_btn1);


        Intent preIntent = getIntent();
        int passId= preIntent.getIntExtra("userId",-1);
        User curUsr = Utils.userDAO.findUserByID(passId);
        String info=curUsr.getPersonalInfo();

        // Set text from memory
        personalInfoEditText.setText(info);

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = personalInfoEditText.getText().toString();
                PersonalInfo.setPersonalInfo(info); // Store in memory

                Toast.makeText(AdminSetPersonalInfo.this, "Info saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminSetPersonalInfo.this, AdminMain.class);
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                User curUsr = Utils.userDAO.findUserByID(passId);
                curUsr.setPersonalInfo(info);
                startActivity(intent);
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just go back to main admin activity
                Intent intent = new Intent(AdminSetPersonalInfo.this, AdminMain.class);
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                startActivity(intent);
                finish();
            }
        });


        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(AdminSetPersonalInfo.this, AdminMain.class);
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                startActivity(intent);
                finish();
            }
        });
    }
}
