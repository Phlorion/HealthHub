package com.example.healthhub.Utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GMRoute {
    String jsonString;

    public GMRoute(String jsonString) {
        this.jsonString = jsonString;
    }

    /**
     * Extract the polylines from the JSON.
     * Each JSON has a "routes" array.
     * Each element in the routes array has a "legs" array.
     * Each element in the legs array has a "steps" array.
     * In the steps array are the polylines with their encoded points.
     * @return An arraylist with all the LatLng objects our polylines pass through
     */
    public List<com.google.android.gms.maps.model.LatLng> extractPolylines() {
        try {
            JsonElement rootElement = JsonParser.parseString(jsonString);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray routesArray = rootObject.getAsJsonArray("routes");

            if (routesArray != null && !routesArray.isEmpty()) {
                JsonObject firstRoute = routesArray.get(0).getAsJsonObject();
                JsonArray legsArray = firstRoute.getAsJsonArray("legs");

                if (legsArray != null && !legsArray.isEmpty()) {
                    JsonObject firstLeg = legsArray.get(0).getAsJsonObject();
                    JsonArray stepsArray = firstLeg.getAsJsonArray("steps");

                    if (stepsArray != null && !stepsArray.isEmpty()) {
                        List<com.google.android.gms.maps.model.LatLng> decodedPath = new ArrayList<>(); // array with all the decoded polyline objects

                        // get all steps
                        for (JsonElement element : stepsArray) {
                            JsonObject step = element.getAsJsonObject(); // parse as json object
                            JsonObject polylineObject = step.getAsJsonObject("polyline"); // get the polyline and parse as json object as well
                            String encodedPolyline = polylineObject.get("points").getAsString(); // get a string with the encoded points of the polyline

                            // encodedPolyline string
                            Log.d("Polyline", "Encoded Polyline: " + encodedPolyline);

                            // decode the encoded string into an array of LatLng objects
                            // for each LatLng object
                            for (LatLng latLng : PolylineEncoding.decode(encodedPolyline)) {
                                decodedPath.add(convertToAndroidLatLng(latLng)); // add the LatLng object to the array
                            }
                        }

                        Log.d("Polyline", "Decoded Path Points: " + decodedPath.size());
                        return decodedPath;

                    } else {
                        Log.w("Polyline", "No steps found in the route.");
                    }
                } else {
                    Log.w("Polyline", "No legs found in the route.");
                }
            } else {
                Log.w("Polyline", "No routes found in the JSON.");
            }

        } catch (Exception e) {
            Log.e("JSON Parsing", "Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Convert the com.google.maps.model LatLng to com.google.android.gms.maps.model LatLng
     * @param mapsApiLatLng The com.google.maps.model LatLng object
     * @return The com.google.android.gms.maps.model LatLng object
     */
    private com.google.android.gms.maps.model.LatLng convertToAndroidLatLng(com.google.maps.model.LatLng mapsApiLatLng) {
        if (mapsApiLatLng == null) {
            return null;
        }
        double latitude = mapsApiLatLng.lat;
        double longitude = mapsApiLatLng.lng;
        return new com.google.android.gms.maps.model.LatLng(latitude, longitude);
    }
}
