package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminLogIn extends AppCompatActivity {

    Button loginBtn;
    TextView registerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        loginBtn = findViewById(R.id.admin_login_btn1);
        registerTV = findViewById(R.id.admin_login_tv4);

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogIn.this, AdminSignUp.class);
                startActivity(intent);
            }
        });
    }
}