package com.example.healthhub;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthhub.DAO.Home;
import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AdminSetHome extends AppCompatActivity implements OnMapReadyCallback {

    Button backBtn;
    SupportMapFragment mapsFragment;
    EditText countryET;
    EditText cityET;
    EditText streetET;
    EditText numberET;
    EditText postalET;
    Button saveBtn;

    GoogleMap map;
    Marker marker;

    // Logged in user
    User user;

    // User's home
    Home userHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sethome);

        // get data from previous activity
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            int userId = receivedIntent.getIntExtra("userId", -1);
            user = Utils.userDAO.findUserByID(userId);
        }

        backBtn = findViewById(R.id.admin_sethome_btn1);
        saveBtn = findViewById(R.id.admin_sethome_btn2);
        countryET = findViewById(R.id.admin_sethome_et1);
        cityET = findViewById(R.id.admin_sethome_et2);
        streetET = findViewById(R.id.admin_sethome_et3);
        numberET = findViewById(R.id.admin_sethome_et4);
        postalET = findViewById(R.id.admin_sethome_et5);

        // check if the user has already entered his home address
        userHome = Utils.homeDAO.findHomeByUserID(user.getId());
        if (userHome != null) {
            countryET.setText(userHome.getCountry());
            cityET.setText(userHome.getCity());
            streetET.setText(userHome.getStreet());
            numberET.setText(userHome.getNumber());
            postalET.setText(userHome.getPostal());
        }

        mapsFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.admin_sethome_fragment);
        assert mapsFragment != null;
        mapsFragment.getMapAsync(this);

        // back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // save button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = countryET.getText().toString();
                String city = cityET.getText().toString();
                String street = streetET.getText().toString();
                String number = numberET.getText().toString();
                String postal = postalET.getText().toString();

                // check that no field is empty
                if (country.isBlank() || city.isBlank() || street.isBlank() || number.isBlank() || postal.isBlank()) {
                    Toast.makeText(AdminSetHome.this, "Some fields are missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                // set home
                if (user != null) { // if logged in user
                    if (userHome == null) {// if user hasn't set a home yet, create new home
                        userHome = new Home(user.getId(), country, city, street, number, postal);
                    }
                    else { // if user has set a home previously, update existing one
                        userHome.updateHome(country, city, street, number, postal);
                    }
                }

                setLocationMap(country, city, street, number, postal);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // set map style
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        // when the map is ready, load googleMap to our variable
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);

        // if the user has entered his home address, then find location on map
        if (userHome != null)
            setLocationMap(countryET.getText().toString(), cityET.getText().toString(), streetET.getText().toString(), numberET.getText().toString(), postalET.getText().toString());
    }

    private void setLocationMap(String country, String city, String street, String number, String postal) {
        // assemble location string
        String location = street + " " + number + " " + city + " " + postal + " " + country;

        // look up location on maps
        Geocoder geocoder = new Geocoder(AdminSetHome.this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            Toast.makeText(AdminSetHome.this, "An error occurred", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        assert addresses != null;
        Address address = addresses.get(0);
        LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude()); // get latitude and longitude of our address

        // add marker
        // if marker exists then clear
        if (marker != null) marker.remove();
        marker = map.addMarker(new MarkerOptions().position(latlng));

        // move camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.f));
    }
}