package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.DAO.Contact;
import com.example.healthhub.Utils.Utils;

public class AdminAddContactActivity extends AppCompatActivity {

    EditText nameEditText, phoneEditText;
    Button saveBtn, cancelBtn,goBack;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.edit_contact_name);
        phoneEditText = findViewById(R.id.edit_contact_phone);
        saveBtn = findViewById(R.id.save_button);
        cancelBtn = findViewById(R.id.cancel_button);
        goBack = findViewById(R.id.admin_setpersonal_btn1);

        userId = getIntent().getIntExtra("userId", -1);

        saveBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please enter both name and phone.", Toast.LENGTH_SHORT).show();
                return;
            }

            new Contact(name, phone, userId);

            Intent resultIntent = new Intent(AdminAddContactActivity.this,AdminSetFavoriteContact.class);
            resultIntent.putExtra("newName", name);
            resultIntent.putExtra("newPhone", phone);
            Intent preIntent = getIntent();
            int passId= preIntent.getIntExtra("userId",-1);
            resultIntent.putExtra("userId",passId);
            setResult(RESULT_OK, resultIntent);
            startActivity(resultIntent);
            finish();
        });

        cancelBtn.setOnClickListener(v -> {
            Intent preIntent = getIntent();
            int passId= preIntent.getIntExtra("userId",-1);
            Intent intent = new Intent(AdminAddContactActivity.this, AdminSetFavoriteContact.class);
            intent.putExtra("userId",passId);
            startActivity(intent
            );
            setResult(RESULT_CANCELED);
            finish();
        });

        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent preIntent = getIntent();
                int passId= preIntent.getIntExtra("userId",-1);
                Intent intent = new Intent(AdminAddContactActivity.this, AdminSetFavoriteContact.class);
                intent.putExtra("userId",passId);
                startActivity(intent);
                finish();
            }
        });

    }
}
