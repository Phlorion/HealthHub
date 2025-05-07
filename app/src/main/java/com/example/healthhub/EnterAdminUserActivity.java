package com.example.healthhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.Home;
import com.example.healthhub.DAO.User;
import com.example.healthhub.DAO.UserDAO;
import com.example.healthhub.Utils.Utils;

public class EnterAdminUserActivity extends AppCompatActivity {

    Button adminBtn;
    Button userBtn;
    TextView headerTV;

    public EnterAdminUserActivity() {


        User AdminUSER = new User("Admin","admin","admin");
        Home AdminHome= new Home(AdminUSER.getId(),"Greece","Athens","Fokon","11",  "143 41");


        User testUser = new User("George", "georgeavrabos@gmail.com", "george111");
        Home testHome = new Home(testUser.getId(), "Greece", "Athens", "28is Oktovriou", "76", "10434");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check application storage for existing user data
        int userId = Utils.getStoredUserId(getApplicationContext());
        if (Utils.userDAO.findUserByID(userId) != null) { // valid user id
            Intent intent = new Intent(EnterAdminUserActivity.this, UserGetHome.class); //TODO: Change destination
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_enter_admin_user);

        // set gradient for header
        headerTV = findViewById(R.id.enter_admin_user_tv2);
        TextPaint paint = headerTV.getPaint();
        float width = paint.measureText(headerTV.getText().toString());
        Shader headerShader = new LinearGradient(0, 0, width, headerTV.getTextSize(), new int[] {
                Color.parseColor("#5A8E11"),
                Color.parseColor("#2A4209")
        }, null, Shader.TileMode.CLAMP);
        headerTV.getPaint().setShader(headerShader);

        adminBtn = findViewById(R.id.enter_admin_user_btn1);
        userBtn = findViewById(R.id.enter_admin_user_btn2);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterAdminUserActivity.this, AdminLogIn.class);
                startActivity(intent);
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterAdminUserActivity.this, UserLogIn.class);
                startActivity(intent);
            }
        });
    }
}