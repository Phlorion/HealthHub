package com.example.healthhub.Utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.healthhub.BuildConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GMDistanceCalculator {
    private static final String TAG = "GMDistanceCalculator";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public interface onDistanceCalculatedListener {
        void onDistanceCalculated(DistanceMatrix distanceMatrix);
        void onDistanceCalculateFailed(Exception e);
    }

    /**
     * Calculate the Distance Matrix between two locations.
     * @param origin Origin location's latitude and longitude.
     * @param destination Destination's latitude and longitude.
     * @param listener Event listener for when Distance Matrix request is fulfilled.
     */
    public void calculateDistanceMatrix(LatLng origin, LatLng destination, onDistanceCalculatedListener listener) {
        executorService.execute(() -> {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(BuildConfig.MAPS_API_KEY)
                    .build();

            try {
                DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
                        .origins(convertToMapsAPILatLng(origin))
                        .destinations(convertToMapsAPILatLng(destination))
                        .mode(TravelMode.DRIVING)
                        .units(Unit.METRIC);

                DistanceMatrix resultMatrix = request.await();
                context.shutdown();

                mainThreadHandler.post(() -> listener.onDistanceCalculated(resultMatrix));

            } catch (Exception e) {
                Log.e(TAG, "Error calculating distance: " + e.getMessage());
                context.shutdown();

                mainThreadHandler.post(() -> listener.onDistanceCalculateFailed(e));
            }
        });
    }

    /**
     * Convert com.google.android.gms.maps.model LatLng to com.google.maps.model LatLng
     * @param androidApiLatLng The com.google.android.gms.maps.model LatLng object
     * @return The com.google.maps.model LatLng object
     */
    private com.google.maps.model.LatLng convertToMapsAPILatLng(com.google.android.gms.maps.model.LatLng androidApiLatLng) {
        if (androidApiLatLng == null) {
            return null;
        }
        double latitude = androidApiLatLng.latitude;
        double longitude = androidApiLatLng.longitude;
        return new com.google.maps.model.LatLng(latitude, longitude);
    }
}
