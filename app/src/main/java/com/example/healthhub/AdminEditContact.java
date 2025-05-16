package com.example.healthhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.DAO.Contact;
import com.example.healthhub.Utils.Utils;

import java.util.List;

public class AdminEditContact extends AppCompatActivity {

    EditText editName, editPhone;
    Button btnSave;
    int contactId;
    Button saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editName = findViewById(R.id.edit_contact_name);
        editPhone = findViewById(R.id.edit_contact_phone);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Get data passed from the previous screen
        String name = getIntent().getStringExtra("contactName");
        String phone = getIntent().getStringExtra("contactPhone");

        editName.setText(name);
        editPhone.setText(phone);

        saveButton.setOnClickListener(v -> {
            String updatedName = editName.getText().toString().trim();
            String updatedPhone = editPhone.getText().toString().trim();

            // Return result
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedName", updatedName);
            resultIntent.putExtra("updatedPhone", updatedPhone);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
    }


