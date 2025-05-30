package com.example.healthhub;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.Contact;
import com.example.healthhub.DAO.ContactDAO;
import com.example.healthhub.Utils.Utils;

import java.util.List;

public class UserSos extends AppCompatActivity{

    private Button btnSendSms, btnCallFavorites, btnCallAmbulance,goBack;

    private static final int REQUEST_SMS_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sos);

        Intent preIntent = getIntent();
        int passId= preIntent.getIntExtra("userId",-1);

        btnSendSms = findViewById(R.id.btnSendSms);
        btnCallFavorites = findViewById(R.id.btnCallFavorites);
        btnCallAmbulance = findViewById(R.id.btnCallAmbulance);
        goBack = findViewById(R.id.admin_setpersonal_btn1);


        btnSendSms.setOnClickListener(v -> sendSmsToFavorites(passId));
        btnCallFavorites.setOnClickListener(v -> callFavorites(passId));
        btnCallAmbulance.setOnClickListener(v -> callAmbulance());

        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(UserSos.this, UserMain.class);
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                intent.putExtra("userId",passId);
                startActivity(intent);
                finish();
            }
        });

    }


    private void sendSmsToFavorites(int userId) {
        // Check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
            return;
        }

        List<Contact> contacts = Utils.contactDAO.getContacts();
        SmsManager smsManager = SmsManager.getDefault();

        if (contacts.isEmpty()){
            Toast.makeText(this, "No contacts!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean smsSent = false;

        for (Contact contact : contacts) {
            if (contact.getUserID() == userId) {
                smsManager.sendTextMessage(contact.getPhoneNum(), null, "SOS! I need help!", null, null);
                smsSent = true;
            }
        }

        if (smsSent) {
            Toast.makeText(this, "SMS sent to favorite contacts", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No favorite contacts found for this user", Toast.LENGTH_SHORT).show();
        }
    }


    private void callFavorites(int userId) {
        List<Contact> contacts = Utils.contactDAO.getContacts();

        if (contacts.isEmpty()){
            Toast.makeText(this, "No contacts!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Contact contact : Utils.contactDAO.getContacts()) {
            if (contact.getUserID() == userId) {
                String phoneNumber = contact.getPhoneNum();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    // Optionally, implement callback or delay to call next contact
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }}

    }

    private void callAmbulance() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0123465789")); // Fixed ambulance number

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }


}
