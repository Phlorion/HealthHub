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

import com.example.healthhub.DAO.User;
import com.example.healthhub.DAO.UserDAO;
import com.example.healthhub.Utils.Utils;

public class EnterAdminUserActivity extends AppCompatActivity {

    Button adminBtn;
    Button userBtn;

    public EnterAdminUserActivity() {
        User testUser = new User("George", "georgeavrabos@gmail.com", "george111");
        Utils.userDAO.addUser(testUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_admin_user);


        adminBtn = findViewById(R.id.enter_admin_user_btn1);
        userBtn = findViewById(R.id.enter_admin_user_btn2);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterAdminUserActivity.this, AdminLogIn.class);
                startActivity(intent);
            }
        });
    }
}