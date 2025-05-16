package com.example.healthhub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.DAO.Contact;
import com.example.healthhub.Models.ContactModel;
import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AdminSetFavoriteContact extends AppCompatActivity {

    LinearLayout contactsContainer;
    User user;
    List<Contact> userContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_favorite_contact); // Make sure this layout has a LinearLayout with id contacts_linear_layout

        contactsContainer = findViewById(R.id.contacts_linear_layout);

        Intent receivedIntent = getIntent();
        int userId = receivedIntent.getIntExtra("userId", -1);

        user = Utils.userDAO.findUserByID(userId);
        if (user != null) {
            loadFavoriteContactsForUser(user.getId());
            setupContacts();
        }
    }

    private void loadFavoriteContactsForUser(int userId) {
        userContacts.clear();

        for (Contact contact : Utils.contactDAO.getContacts()) {
            if (contact.getUserID() == userId) {
                userContacts.add(contact);
            }
        }
    }

    private void setupContacts() {
        contactsContainer.removeAllViews();

        for (Contact contact : userContacts) {
            View contactCard = getLayoutInflater().inflate(R.layout.contact_card, contactsContainer, false);

            TextView nameTextView = contactCard.findViewById(R.id.contactNameTextView);
            TextView numberTextView = contactCard.findViewById(R.id.contactNumberTextView);
            ImageButton removeButton = contactCard.findViewById(R.id.removeContactButton);
            ImageButton editButton= contactCard.findViewById(R.id.editContactButton);

            nameTextView.setText(contact.getName());
            numberTextView.setText(contact.getPhoneNum());

            editButton.setOnClickListener(v -> {
                Toast.makeText(this,"EDITING",Toast.LENGTH_SHORT).show();
            });

            removeButton.setOnClickListener(v -> {
                // Inflate your custom dialog layout
                View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_remove_contact, null);

                // Build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                // Get buttons from layout
                Button cancelButton = dialogView.findViewById(R.id.button_cancel);
                Button removeButtonInDialog = dialogView.findViewById(R.id.button_remove);

                // Cancel button just closes dialog
                cancelButton.setOnClickListener(cancelView -> dialog.dismiss());

                // Remove button removes contact from list + UI + DAO
                removeButtonInDialog.setOnClickListener(removeView -> {
                    Utils.contactDAO.removeContact(contact);
                    userContacts.remove(contact);
                    contactsContainer.removeView(contactCard);
                    Toast.makeText(this, "Contact removed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

                dialog.show();
            });

            contactsContainer.addView(contactCard);
        }
    }
}

