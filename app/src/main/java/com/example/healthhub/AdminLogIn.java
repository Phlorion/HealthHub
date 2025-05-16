package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.Utils.Utils;

public class AdminLogIn extends AppCompatActivity {

    Button loginBtn;
    TextView registerTV;

    EditText emailET;
    EditText passET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        loginBtn = findViewById(R.id.admin_login_btn1);
        registerTV = findViewById(R.id.admin_login_tv4);

        emailET = findViewById(R.id.admin_login_et1);
        passET = findViewById(R.id.admin_login_et2);

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogIn.this, AdminSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                // check if user is valid
                if (Utils.userDAO.authUser(email, pass)) {
                    // go to corresponding activity
                    Intent intent = new Intent(AdminLogIn.this, AdminMain.class);
                    intent.putExtra("userId",Utils.userDAO.findUser(email,pass).getId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLogIn.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}