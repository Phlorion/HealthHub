package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;

import java.util.Objects;

public class AdminSignUp extends AppCompatActivity {

    EditText nameET;
    EditText emailET;
    EditText passET;
    EditText confirmPassET;
    CheckBox agreeTermsCB;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        nameET = findViewById(R.id.admin_signup_et1);
        emailET = findViewById(R.id.admin_signup_et2);
        passET = findViewById(R.id.admin_signup_et3);
        confirmPassET = findViewById(R.id.admin_signup_et4);
        agreeTermsCB = findViewById(R.id.admin_signup_cb1);
        signUpBtn = findViewById(R.id.admin_signup_btn1);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();
                String confirmPass = confirmPassET.getText().toString();

                // check that no field is empty
                if (name.isBlank() || email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
                    Toast.makeText(AdminSignUp.this, "Some fields are missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if passwords match
                if (!Objects.equals(pass, confirmPass)) {
                    Toast.makeText(AdminSignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

                // check if agree terms is clicked
                if (!agreeTermsCB.isChecked()) {
                    Toast.makeText(AdminSignUp.this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                // create user
                User user = new User(name, email, pass);
                // add to DAO
                Utils.userDAO.addUser(user);

                Intent intent = new Intent(AdminSignUp.this, AdminLogIn.class);
                startActivity(intent);
            }
        });
    }
}