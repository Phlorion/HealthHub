package com.example.healthhub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.Models.ContactModel;

import java.util.ArrayList;

public class AdminSetFavoriteContact extends AppCompatActivity{

    ArrayList<ContactModel> ContactModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_fav_cntc);

    }

    private void setUpContactModels(){

    }
}
