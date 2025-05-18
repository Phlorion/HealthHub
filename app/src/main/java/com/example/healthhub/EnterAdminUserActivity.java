package com.example.healthhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
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

import com.example.healthhub.DAO.Contact;
import com.example.healthhub.DAO.Home;
import com.example.healthhub.DAO.Medication;
import com.example.healthhub.DAO.User;
import com.example.healthhub.DAO.UserDAO;
import com.example.healthhub.Utils.Utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class EnterAdminUserActivity extends AppCompatActivity {

    Button adminBtn;
    Button userBtn;
    TextView headerTV;

    public EnterAdminUserActivity() {


        User AdminUSER = new User("Admin","admin","admin");
        Home AdminHome= new Home(AdminUSER.getId(),"Greece","Athens","Fokon","11",  "143 41");


        User testUser = new User("George", "georgeavrabos@gmail.com", "george111");
        Home testHome = new Home(testUser.getId(), "Greece", "Athens", "28is Oktovriou", "76", "10434");
        LocalDate fromDate = null; // Example start date: May 15, 2025
        LocalDate toDate = null; // Example start date: May 15, 2025
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fromDate = LocalDate.parse(LocalDate.of(2025, 5, 15).format(formatter),formatter);
            toDate = LocalDate.parse(LocalDate.of(2025, 5, 22).format(formatter),formatter);   // Example end date: May 22, 2025
        }
        List<String> days = Arrays.asList("Mon", "Wed", "Fri");
        List<String> time = Arrays.asList("08:00 pm", "02:00 am", "09:00 pm");
        Medication med2 = new Medication(testUser.getId(),"Name 2","Yes Quantity 2", fromDate, toDate, days, time);
        med2.saveMedicationToDAO();
        Medication med1 = new Medication(testUser.getId(),"Name", "Yes Quantity 1",fromDate, toDate, days, time);
        med1.saveMedicationToDAO();
        Medication med3 = new Medication(testUser.getId(),"Name 3","Yes Quantity 3", fromDate, toDate, List.of("3"), time);
        med3.saveMedicationToDAO();


        Contact contact1 = new Contact("John Doe", "12345678",AdminUSER.getId());
        Contact contact2 = new Contact("Gustavo Fring", "87654321",AdminUSER.getId());

        Contact contact3 = new Contact("John Doe", "12345678",testUser.getId());
        Contact contact4 = new Contact("Gustavo Fring", "87654321",testUser.getId());

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check application storage for existing user data
        int userId = Utils.getStoredUserId(getApplicationContext());
        if (Utils.userDAO.findUserByID(userId) != null) { // valid user id
            Intent intent = new Intent(EnterAdminUserActivity.this, UserGetNearHealthFacilities.class); //TODO: Change destination
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