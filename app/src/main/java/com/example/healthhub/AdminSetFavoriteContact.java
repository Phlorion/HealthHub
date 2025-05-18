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
        setContentView(R.layout.activity_admin_set_favorite_contact);

        contactsContainer = findViewById(R.id.activity_admin_set_favorite_contact);

        Intent receivedIntent = getIntent();
        int userId = receivedIntent.getIntExtra("userId", -1);

        user = Utils.userDAO.findUserByID(userId);
        if (user != null) {
//            LOAD MAKE THE CONTACTS FOR THE USER
            loadFavoriteContactsForUser(user.getId());

//          SETUPS TEH UI
            setupContacts();
        }
        ImageButton addContactButton = (ImageButton) findViewById(R.id.addContactButtonID);
        addContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminAddContactActivity.class);
            intent.putExtra("userId", user.getId());
            startActivityForResult(intent, 2);
        });
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

        LinearLayout contactsContainer = findViewById(R.id.contactsContainer);
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
                Intent intent = new Intent(AdminSetFavoriteContact.this, AdminEditContact.class);
                intent.putExtra("contactName", contact.getName());
                intent.putExtra("contactPhone", contact.getPhoneNum());
                startActivityForResult(intent, 1);
            });

            removeButton.setOnClickListener(v -> {
                // This is the popup when trying to remove a contact
                View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_remove_contact, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // edit contact result (already done before)
            String updatedName = data.getStringExtra("updatedName");
            String updatedPhone = data.getStringExtra("updatedPhone");

            for (Contact c : Utils.contactDAO.getContacts()) {
                if (c.getUserID() == user.getId()) {
                    if (c.getName().equals(updatedName) || c.getPhoneNum().equals(updatedPhone)) {
                        c.updateContact(updatedName, updatedPhone);
                        break;
                    }
                }
            }

            loadFavoriteContactsForUser(user.getId());
            setupContacts();

        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // add new contact result
            loadFavoriteContactsForUser(user.getId());
            setupContacts();
        }
    }
}

