package com.example.healthhub;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthhub.DAO.User;
import com.example.healthhub.Utils.GMRoute;
import com.example.healthhub.Utils.NetworkHelper;
import com.example.healthhub.Utils.Utils;
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
import com.google.android.libraries.places.api.model.Place;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HealthFacilityDetails extends AppCompatActivity implements OnMapReadyCallback {

    private final static String TAG = "HealthFacDetails";

    User user;

    String name;
    String address;
    String distance;
    String primaryType;
    Location currentLocation;
    LatLng location;
    Place.BusinessStatus businessStatus;
    int image;

    // activity
    Button backBtn;
    TextView healthfacNameTV;
    ImageView imageIcon;
    TextView stateTV;
    TextView currentLocationTV;
    TextView healthfacLoactionTV;
    Button goBtn;

    // maps
    SupportMapFragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_facility_details);

        // get data from previous activity
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            int userId = receivedIntent.getIntExtra("userId", -1);
            user = Utils.userDAO.findUserByID(userId);
            currentLocation = receivedIntent.getParcelableExtra("user_current_location");

            name = receivedIntent.getStringExtra("fac_name");
            address = receivedIntent.getStringExtra("fac_address");
            distance = receivedIntent.getStringExtra("fac_distance");
            primaryType = receivedIntent.getStringExtra("fac_primaryType");
            location = receivedIntent.getParcelableExtra("fac_location");
            businessStatus = receivedIntent.getParcelableExtra("fac_businessStatus");
            image = receivedIntent.getIntExtra("fac_image", -1);
        }

        backBtn = findViewById(R.id.healthfac_btn1);
        goBtn = findViewById(R.id.healthfac_btn2);
        healthfacNameTV = findViewById(R.id.healthfac_tv6);
        currentLocationTV = findViewById(R.id.healthfac_tv3);
        healthfacLoactionTV = findViewById(R.id.healthfac_tv5);
        stateTV = findViewById(R.id.healthfac_tv7);
        imageIcon = findViewById(R.id.healthfac_img1);

        healthfacNameTV.setText(name);
        currentLocationTV.setText(getLocationAddress(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
        healthfacLoactionTV.setText(address);
        stateTV.setText(businessStatus.toString());
        imageIcon.setImageResource(image);

        mapsFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.healthfac_mapsFragment);
        assert mapsFragment != null;
        mapsFragment.getMapAsync(this);

        /*Log.d(TAG, name);
        Log.d(TAG, address);
        Log.d(TAG, distance);
        Log.d(TAG, primaryType == null ? "None" : primaryType);
        Log.d(TAG, currentLocation.toString());
        Log.d(TAG, location.toString());
        Log.d(TAG, businessStatus.toString());*/

        // back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocation != null && location != null) { // if both home address registered and current location can be tracked
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/" + currentLocation.getLatitude() + "," + currentLocation.getLongitude()
                            + "/" + location.latitude + "," + location.longitude);

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Either current or home location cannot be specified.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // set map style
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);

        // set current location marker
        LatLng currentLatLng = null;
        if (currentLocation != null) {
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            googleMap.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.user))));
        }

        // set health facility marker
        if (location != null) {
            googleMap.addMarker(new MarkerOptions().position(location));
        }

        // move camera so it fits both markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        assert currentLatLng != null;
        builder.include(currentLatLng);
        assert location != null;
        builder.include(location);
        LatLngBounds bounds = builder.build();

        View mapView = mapsFragment.getView();
        assert mapView != null;
        int width = mapView.getWidth();
        int height = mapView.getHeight();
        int padding = (int) (width * 0.1);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        // draw route
        NetworkHelper networkHelper = new NetworkHelper();
        String url = getDirectionsUrl(currentLatLng, location); // construct URL
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

                    googleMap.addPolyline(polylineOptions);
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
            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        assert addresses != null;

        return addresses.get(0).getAddressLine(0);
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
}