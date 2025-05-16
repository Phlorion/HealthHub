package com.example.healthhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.DAO.Contact;
import com.example.healthhub.Utils.Utils;

import java.util.List;

public class AdminEditContact extends AppCompatActivity {

        private LinearLayout contactsContainer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.testing);

            contactsContainer = findViewById(R.id.testing);

            List<Contact> contacts = getContactsFromDao(); // Replace with your actual DAO call

            for (Contact contact : contacts) {
                addContactCard(contact);
            }
        }

        private void addContactCard(Contact contact) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View cardView = inflater.inflate(R.layout.contact_card, contactsContainer, false);

            TextView nameTextView = cardView.findViewById(R.id.contactNameTextView);
            TextView numberTextView = cardView.findViewById(R.id.contactNumberTextView);

            ImageButton editButton = cardView.findViewById(R.id.editContactButton);
            ImageButton removeButton = cardView.findViewById(R.id.removeContactButton);

            nameTextView.setText(contact.getName());
            numberTextView.setText(contact.getPhoneNum());

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Handle edit logic
                    Toast.makeText(AdminEditContact.this, "Edit " + contact.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRemoveDialog(contact);
                }
            });

            contactsContainer.addView(cardView);
        }

        private void showRemoveDialog(Contact contact) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this contact from the Favorite Contacts?")
                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // TODO: Actually remove the contact
                            Toast.makeText(AdminEditContact.this, "Removed " + contact.getName(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }

        private List<Contact> getContactsFromDao() {

            return Utils.contactDAO.getContacts();
        }
    }


