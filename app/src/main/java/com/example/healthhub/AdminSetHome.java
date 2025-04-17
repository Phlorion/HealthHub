package com.example.healthhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class AdminSetHome extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sethome);

        SupportMapFragment mapsFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.admin_sethome_fragment);
        assert mapsFragment != null;
        mapsFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(37.9838, 23.7275);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}