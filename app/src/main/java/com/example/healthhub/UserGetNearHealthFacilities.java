package com.example.healthhub;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.Adapters.HealthFacilitiesAdapter;
import com.example.healthhub.DAO.User;
import com.example.healthhub.Models.HealthFacilityModel;
import com.example.healthhub.Utils.GMDistanceCalculationManager;
import com.example.healthhub.Utils.GMDistanceCalculator;
import com.example.healthhub.Utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.CircularBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchNearbyRequest;
import com.google.maps.model.Distance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class UserGetNearHealthFacilities extends AppCompatActivity {

    // Google Maps
    private final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    // Places API
    private PlacesClient placesClient;
    private List<Place> places;
    private HashMap<String, Distance> placesDistances;
    private final int RADIUS = 3000;
    private final int MAX_COUNT = 20;

    // Logged in User
    private User user;

    // Activity
    private FrameLayout allButton;
    private FrameLayout hospitalsButton;
    private FrameLayout pharmaciesButton;

    private ImageView allIcon;
    private ImageView hospitalsIcon;
    private ImageView pharmaciesIcon;

    private Button backBtn;

    private enum FILTERS {
        ALL {
            @Override
            List<String> getIncludeTypes() {
                return Arrays.asList("pharmacy", "hospital");
            }
        },
        HOSPITAL {
            @Override
            List<String> getIncludeTypes() {
                return List.of("hospital");
            }
        },
        PHARMACY {
            @Override
            List<String> getIncludeTypes() {
                return List.of("pharmacy");
            }
        };

        abstract List<String> getIncludeTypes();
    }

    private FILTERS lastFilter = FILTERS.ALL; // changes to the last filter value, depending on what button we pressed

    // Recycler View
    ArrayList<HealthFacilityModel> healthFacilityModels;
    RecyclerView healthFacilitiesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_near_health_facilities);

        // get data from previous activity
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            int userId = receivedIntent.getIntExtra("userId", -1);
            user = Utils.userDAO.findUserByID(userId);
        }

        allButton = findViewById(R.id.user_healthfac_fl1);
        hospitalsButton = findViewById(R.id.user_healthfac_fl2);
        pharmaciesButton = findViewById(R.id.user_healthfac_fl3);
        allIcon = findViewById(R.id.user_healthfac_img1);
        hospitalsIcon = findViewById(R.id.user_healthfac_img2);
        pharmaciesIcon = findViewById(R.id.user_healthfac_img3);
        backBtn = findViewById(R.id.user_healthfac_btn1);

        healthFacilitiesRV = findViewById(R.id.user_healthfac_recycler1);

        // when activity loaded, go to all by default
        // set unselected colors for other
        hospitalsButton.setBackground(getDrawable(R.drawable.health_facilities_middle_btn_unselected));
        hospitalsIcon.setColorFilter(getColor(R.color.app_main));
        pharmaciesButton.setBackground(getDrawable(R.drawable.health_facilities_right_btn_unselected));
        pharmaciesIcon.setColorFilter(getColor(R.color.app_main));
        // set selected colors for this
        allButton.setBackground(getDrawable(R.drawable.health_facilities_left_btn_selected));
        allIcon.setColorFilter(getColor(R.color.white));

        // back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // handle all button press
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set unselected colors for other
                hospitalsButton.setBackground(getDrawable(R.drawable.health_facilities_middle_btn_unselected));
                hospitalsIcon.setColorFilter(getColor(R.color.app_main));
                pharmaciesButton.setBackground(getDrawable(R.drawable.health_facilities_right_btn_unselected));
                pharmaciesIcon.setColorFilter(getColor(R.color.app_main));

                // set selected colors for this
                allButton.setBackground(getDrawable(R.drawable.health_facilities_left_btn_selected));
                allIcon.setColorFilter(getColor(R.color.white));

                lastFilter = FILTERS.ALL;
            }
        });

        // handle hospitals button press
        hospitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set unselected colors for other
                allButton.setBackground(getDrawable(R.drawable.health_facilities_left_btn_unselected));
                allIcon.setColorFilter(getColor(R.color.app_main));
                pharmaciesButton.setBackground(getDrawable(R.drawable.health_facilities_right_btn_unselected));
                pharmaciesIcon.setColorFilter(getColor(R.color.app_main));

                // set selected colors for this
                hospitalsButton.setBackground(getDrawable(R.drawable.health_facilities_middle_btn_selected));
                hospitalsIcon.setColorFilter(getColor(R.color.white));

                lastFilter = FILTERS.HOSPITAL;
            }
        });

        // handle pharmacies button press
        pharmaciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set unselected colors for other
                allButton.setBackground(getDrawable(R.drawable.health_facilities_left_btn_unselected));
                allIcon.setColorFilter(getColor(R.color.app_main));
                hospitalsButton.setBackground(getDrawable(R.drawable.health_facilities_middle_btn_unselected));
                hospitalsIcon.setColorFilter(getColor(R.color.app_main));

                // set selected colors for this
                pharmaciesButton.setBackground(getDrawable(R.drawable.health_facilities_right_btn_selected));
                pharmaciesIcon.setColorFilter(getColor(R.color.white));

                lastFilter = FILTERS.PHARMACY;
            }
        });

        // init places client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        placesClient = Places.createClient(this);

        // get last known location of client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    /**
     * Set the healthFacilityModels arraylist for the display of the recycler view items.
     */
    private void setHealthFacilityModels() {
        healthFacilityModels = new ArrayList<>();

        for (Place p : places) {
            String distance = placesDistances.get(p.getId()).humanReadable;
            // if we got the distance from this place
            if (distance != null) {
                int imageID;
                if (FILTERS.HOSPITAL.getIncludeTypes().contains(p.getPrimaryType()))
                    imageID = R.drawable.hospital_svgrepo;
                else if (FILTERS.PHARMACY.getIncludeTypes().contains(p.getPrimaryType()))
                    imageID = R.drawable.pharmacy_icon_svgrepo;
                else
                    imageID = R.drawable.arrows_maximize_svgrepo;
                HealthFacilityModel model = new HealthFacilityModel(p.getId(), p.getDisplayName(), p.getFormattedAddress(), distance, imageID);
                healthFacilityModels.add(model);
            }
        }

        // sort healthFacilityModels array based on distances hashmap
        healthFacilityModels.sort(new Comparator<HealthFacilityModel>() {
            @Override
            public int compare(HealthFacilityModel model1, HealthFacilityModel model2) {
                long value1 = placesDistances.get(model1.getId()).inMeters;
                long value2 = placesDistances.get(model2.getId()).inMeters;

                return Long.compare(value1, value2);
            }
        });
    }

    /**
     * After getting client's current location, search for all nearby health facilities.
     * @param latitude Current latitude.
     * @param longitude Current longitude.
     * @param radius The radius in meters in which to search for the facilities.
     * @param maxResultCount Max amount of results contained in the response.
     */
    private void searchNearbyHealthFacilities(double latitude, double longitude, int radius, int maxResultCount, FILTERS filter) {
        // Define a list of fields to include in the response for each returned place
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.PRIMARY_TYPE, Place.Field.FORMATTED_ADDRESS, Place.Field.LOCATION, Place.Field.BUSINESS_STATUS);

        // Define the search area
        LatLng center = new LatLng(latitude, longitude);
        CircularBounds circle = CircularBounds.newInstance(center, radius);

        // Define a list of types to include
        final List<String> includedTypes = filter.getIncludeTypes();

        // Use the builder to create a SearchNearbyRequest object
        final SearchNearbyRequest searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(maxResultCount)
            .build();

        // Call placesClient.searchNearby() to perform the search
        placesClient.searchNearby(searchNearbyRequest)
                .addOnSuccessListener(response -> {
                    // get a list response of all the places found
                    places = new ArrayList<>();

                    // remove those who are closed
                    for (Place p : response.getPlaces()) {
                        if (p.getBusinessStatus() == Place.BusinessStatus.OPERATIONAL) {
                            places.add(p);
                        }
                    }

                    // set the GMDistanceCalculator to get the distance of every place
                    GMDistanceCalculator gmDistanceCalculator = new GMDistanceCalculator();

                    // start the distance calculation process
                    // we use a distance calculator manager, since the calculations are executed in different threads and we want to fetch them when they have all been completed
                    GMDistanceCalculationManager gmDistanceCalculationManager = new GMDistanceCalculationManager(places, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                            gmDistanceCalculator, new GMDistanceCalculationManager.DistanceCalculationCallback() {
                        @Override
                        public void onAllDistancesCalculated(HashMap<String, Distance> distances) {
                            // this method is run when all distances have been calculated
                            // now we can create the recycler view
                            placesDistances = distances; // assign the distances to our arraylist

                            setHealthFacilityModels();
                            HealthFacilitiesAdapter healthFacilitiesAdapter = new HealthFacilitiesAdapter(getApplicationContext(), healthFacilityModels);
                            healthFacilitiesRV.setAdapter(healthFacilitiesAdapter);
                            healthFacilitiesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    });

                    gmDistanceCalculationManager.startCalculations();
                });
    }

    /**
     * Get the last location the device tracked.
     * After acquiring the last known location, get places.
     */
    private void getLastLocation() {
        // request permission again if failed
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        // get last location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    // search for places
                    searchNearbyHealthFacilities(currentLocation.getLatitude(), currentLocation.getLongitude(), RADIUS, MAX_COUNT, lastFilter);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the device to access your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}