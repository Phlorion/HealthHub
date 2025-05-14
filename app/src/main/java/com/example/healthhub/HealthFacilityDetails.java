package com.example.healthhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public class HealthFacilityDetails extends AppCompatActivity {

    private final static String TAG = "HealthFacDetails";

    User user;

    String name;
    String address;
    String distance;
    String primaryType;
    LatLng location;
    Place.BusinessStatus businessStatus;
    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_facility_details);

        // get data from previous activity
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            int userId = receivedIntent.getIntExtra("userId", -1);
            user = Utils.userDAO.findUserByID(userId);

            name = receivedIntent.getStringExtra("fac_name");
            address = receivedIntent.getStringExtra("fac_address");
            distance = receivedIntent.getStringExtra("fac_distance");
            primaryType = receivedIntent.getStringExtra("fac_primaryType");
            location = receivedIntent.getParcelableExtra("fac_location");
            businessStatus = receivedIntent.getParcelableExtra("fac_businessStatus");
            image = receivedIntent.getIntExtra("fac_image", -1);
        }

        Log.d(TAG, name);
        Log.d(TAG, address);
        Log.d(TAG, distance);
        Log.d(TAG, primaryType);
        Log.d(TAG, location.toString());
        Log.d(TAG, businessStatus.toString());
    }
}