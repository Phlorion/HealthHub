package com.example.healthhub;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.healthhub.DAO.Home;
import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.GMRoute;
import com.example.healthhub.Utils.NetworkHelper;
import com.example.healthhub.Utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserGetHome extends AppCompatActivity implements OnMapReadyCallback {

    Button backBtn;
    SupportMapFragment mapsFragment;
    TextView currentAddressTV;
    TextView homeAddressTV;
    Button getHomeBtn;

    // Google Maps
    final int FINE_PERMISSION_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    GoogleMap map;

    // Logged in User
    User user;

    // User's home
    Home userHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gethome);

        // get data from previous activity
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            int userId = receivedIntent.getIntExtra("userId", -1);
            user = Utils.userDAO.findUserByID(userId);
        }

        backBtn = findViewById(R.id.user_gethome_btn1);
        getHomeBtn = findViewById(R.id.user_gethome_btn2);
        currentAddressTV = findViewById(R.id.user_gethome_tv3);
        homeAddressTV = findViewById(R.id.user_gethome_tv5);

        // check if the user has a home address
        userHome = Utils.homeDAO.findHomeByUserID(user.getId());
        if (userHome != null) {
            homeAddressTV.setText(userHome.getStreet() + " " + userHome.getNumber() + ", " + userHome.getCity() + " " + userHome.getPostal() + ", " + userHome.getCountry());
        }

        // get last known location of client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocation != null && userHome != null) { // if both home address registered and current location can be tracked
                    LatLng homeLatLng = getLatLng(userHome.getCountry(), userHome.getCity(), userHome.getStreet(), userHome.getNumber(), userHome.getPostal());
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/" + currentLocation.getLatitude() + "," + currentLocation.getLongitude()
                    + "/" + homeLatLng.latitude + "," + homeLatLng.longitude);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserGetHome.this, "Either current or home location cannot be specified.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // set map style
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        // when the map is ready, load googleMap to our variable
        map = googleMap;
        //map.getUiSettings().setZoomControlsEnabled(false);
        //map.getUiSettings().setScrollGesturesEnabled(false);

        // set current location marker
        LatLng currentLatLng = null;
        if (currentLocation != null) {
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            map.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.user))));

            // set current location textview
            currentAddressTV.setText(getLocationAddress(currentLatLng));
        }

        // set home marker
        LatLng homeLatLng = null;
        if (userHome != null) { // make sure user has set a home address
            homeLatLng = getLatLng(userHome.getCountry(), userHome.getCity(), userHome.getStreet(), userHome.getNumber(), userHome.getPostal());

            map.addMarker(new MarkerOptions().position(homeLatLng));
        }

        // move camera so it fits both markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        assert currentLatLng != null;
        builder.include(currentLatLng);
        assert homeLatLng != null;
        builder.include(homeLatLng);
        LatLngBounds bounds = builder.build();

        View mapView = mapsFragment.getView();
        assert mapView != null;
        int width = mapView.getWidth();
        int height = mapView.getHeight();
        int padding = (int) (width * 0.1);

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        // draw route
        NetworkHelper networkHelper = new NetworkHelper();
        String url = getDirectionsUrl(currentLatLng, homeLatLng); // construct URL
        networkHelper.downloadJson(url, new NetworkHelper.OnDownloadCompleteListener() { // fetch JSON
            // success
            @Override
            public void onDownloadComplete(String jsonString) {
                // Get the json string and pass it to the GMRoute helper class
                // Then call the extractPolylines method
                GMRoute route = new GMRoute(jsonString);
                List<LatLng> decodedPolylines =  route.extractPolylines();
                if (decodedPolylines != null) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(decodedPolylines)
                            .color(Color.RED)
                            .width(10);

                    map.addPolyline(polylineOptions);
                }
            }

            // failed to fetch
            @Override
            public void onDownloadFailed(IOException e) {
                Log.d("JSON", "JSON download failed. Error: " + e.getMessage());
            }
        });
    }

    /**
     * Converts a string address to LatLng
     * @param country country string
     * @param city city string
     * @param street street string
     * @param number number string
     * @param postal postal string
     * @return a LatLng object
     */
    private LatLng getLatLng(String country, String city, String street, String number, String postal) {
        // assemble location string
        String location = street + " " + number + " " + city + " " + postal + " " + country;

        // look up location on maps
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            Toast.makeText(UserGetHome.this, "An error occurred", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        assert addresses != null;
        Address address = addresses.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude()); // get latitude and longitude of our address
    }

    /**
     * Converts a LatLng object to an address string
     * @param latlng LatLng object
     * @return Address string
     */
    private String getLocationAddress(LatLng latlng) {
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException e) {
            Toast.makeText(UserGetHome.this, "An error occurred", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        assert addresses != null;

        return addresses.get(0).getAddressLine(0);
    }

    /**
     * Get the last location the device tracked.
     * After acquiring the last known location, load the map fragment.
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

                    // create map fragment
                    mapsFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.user_gethome_fragment);
                    assert mapsFragment != null;
                    mapsFragment.getMapAsync(UserGetHome.this);
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

    /**
     * Used to get the icon drawable as a bitmap for the markers
     * @param resId The resource ID
     * @return The bitmap created from the given resource
     */
    private Bitmap getBitmapFromDrawable(int resId) {
        Bitmap bitmap = null;
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }

    /**
     * Get the URL to make request for the json file we need
     * @param origin The origin location LatLng
     * @param dest The destination location LatLng
     * @return The crafted URL
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // route origin
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // route destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        String mode = "mode=DRIVING";

        // parameters for the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + BuildConfig.MAPS_API_KEY;

        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}