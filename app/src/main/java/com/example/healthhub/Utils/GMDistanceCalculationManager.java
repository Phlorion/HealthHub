package com.example.healthhub.Utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.model.DistanceMatrix;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GMDistanceCalculationManager {
    private final String TAG = "DistanceCalculator";
    private HashMap<String, String> placesDistances;
    private List<Place> places;
    private LatLng currentLocation;
    private GMDistanceCalculator gmDistanceCalculator;
    private DistanceCalculationCallback callback; // Custom callback

    // Custom callback interface to notify when all calculations are complete
    public interface DistanceCalculationCallback {
        void onAllDistancesCalculated(HashMap<String, String> distances);
    }

    public GMDistanceCalculationManager(List<Place> places, LatLng currentLocation, GMDistanceCalculator gmDistanceCalculator, DistanceCalculationCallback callback) {
        this.places = places;
        this.currentLocation = currentLocation;
        this.gmDistanceCalculator = gmDistanceCalculator;
        this.callback = callback;
        this.placesDistances = new HashMap<>();
    }

    public void startCalculations() {
        if (places == null || places.isEmpty()) {
            Log.d(TAG, "No places to calculate distances for.");
            callback.onAllDistancesCalculated(placesDistances);
            return;
        }

        final AtomicInteger completedCalculations = new AtomicInteger(0); // use AtomicInteger for thread safety
        final int totalPlaces = places.size();

        for (Place p : places) {
            Log.d("Places", p.getId() + ", " + p.getDisplayName() + ", " + p.getPrimaryType() + ", " + p.getBusinessStatus() + ", " + p.getFormattedAddress());

            gmDistanceCalculator.calculateDistanceMatrix(currentLocation, p.getLocation(), new GMDistanceCalculator.onDistanceCalculatedListener() {
                @Override
                public void onDistanceCalculated(DistanceMatrix distanceMatrix) {
                    synchronized (placesDistances) {
                        if (distanceMatrix != null) {
                            Log.d("DistanceCalculator", "Distance: " + distanceMatrix.rows[0].elements[0].distance.humanReadable);
                            placesDistances.put(p.getId(), distanceMatrix.rows[0].elements[0].distance.humanReadable);
                        } else {
                            Log.d("DistanceCalculator", "Invalid Distance Matrix Response.");
                            placesDistances.put(p.getId(), null);
                        }
                    }

                    if (completedCalculations.incrementAndGet() == totalPlaces) {
                        // all calculations are complete, notify the callback
                        callback.onAllDistancesCalculated(placesDistances);
                    }
                }

                @Override
                public void onDistanceCalculateFailed(Exception e) {
                    synchronized (placesDistances) {  // Synchronize access
                        Log.e("DistanceCalculator", "Failed to calculate distance for place " + p.getId() + ":\n" + e.getMessage());
                        placesDistances.put(p.getId(), null);
                    }

                    if (completedCalculations.incrementAndGet() == totalPlaces) {
                        // all calculations are complete, notify the callback
                        callback.onAllDistancesCalculated(placesDistances);
                    }
                }
            });
        }
    }
}
