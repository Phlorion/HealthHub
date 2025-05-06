package com.example.healthhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;

public class UserLogIn extends AppCompatActivity {

    Button loginBtn;
    EditText emailET;
    EditText passET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        loginBtn = findViewById(R.id.admin_login_btn1);
        emailET = findViewById(R.id.admin_login_et1);
        passET = findViewById(R.id.admin_login_et2);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                // check if user is valid
                if (Utils.userDAO.authUser(email, pass)) {
                    int userId = Utils.userDAO.findUser(email, pass).getId(); // get user id

                    // save user data to SharedPreferences
                    Utils.clearLoginSession(getApplicationContext()); // clear storage before re-writing
                    Utils.saveLoginSession(getApplicationContext(), userId);

                    // go to corresponding activity
                    Intent intent = new Intent(UserLogIn.this, UserGetHome.class); // TODO: Change destination
                    intent.putExtra("userId", userId); // pass user id to next activity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserLogIn.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}