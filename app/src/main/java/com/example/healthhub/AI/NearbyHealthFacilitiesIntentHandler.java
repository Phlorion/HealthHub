package com.example.healthhub.AI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NearbyHealthFacilitiesIntentHandler implements IntentHandler {
    @Override
    public boolean canHandle(String intentName) {
        return "locate_hospitals".equals(intentName) ||
                "locate_clinics".equals(intentName) ||
                "locate_pharmacies".equals(intentName);
    }

    @Override
    public String handleIntent(ParsedNLUResult parsedResult, AppActionSpeaker speaker) {
        String response = parsedResult.response;
        String filter = parsedResult.filter; // e.g., "nearby"

        System.out.println("Handling find_hospitals intent with filter: " + filter);
        String toastMessage = "Searching for hospitals.";
        if (filter != null && filter.equals("nearby")) {
            toastMessage += " Applying 'nearby' filter.";
        }
        speaker.showToast(toastMessage);

        if (speaker instanceof Context) {
            Context context = (Context) speaker;
            // Example: Launch Google Maps to search for hospitals
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospitals"); // Default search
            if (filter != null && filter.equals("nearby")) {
                // If you have a way to get current location, you could use it here
                gmmIntentUri = Uri.parse("geo:0,0?q=hospitals+near+me"); // More specific search
            }

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps"); // Try to open in Google Maps

            // Check if there's an app to handle this intent
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                System.out.println("No app found to handle map intent for hospitals.");
                speaker.showToast("Could not open maps to find hospitals.");
                response = "I couldn't open maps to find hospitals. Do you have a map app installed?";
            }
        } else {
            System.out.println("Speaker is not an Activity Context. Cannot start activity directly.");
            speaker.showToast("Cannot find hospitals. Speaker context is not an Activity.");
            response = "I cannot search for hospitals from this context.";
        }

        return response;
    }
}
