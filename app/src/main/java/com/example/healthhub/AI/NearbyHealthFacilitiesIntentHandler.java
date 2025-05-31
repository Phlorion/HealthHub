package com.example.healthhub.AI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.healthhub.UserGetHome;
import com.example.healthhub.UserGetNearHealthFacilities;
import com.example.healthhub.Utils.Utils;

public class NearbyHealthFacilitiesIntentHandler implements IntentHandler {
    @Override
    public boolean canHandle(String intentName) {
        return "locate_hospitals".equals(intentName) ||
                "locate_clinics".equals(intentName) ||
                "locate_pharmacies".equals(intentName);
    }

    @Override
    public String handleIntent(ParsedNLUResult parsedResult, AppActionSpeaker speaker, Context appContext) {
        String response = parsedResult.response;
        String filter = parsedResult.filter; // e.g., "nearby"

        /*System.out.println("Handling find_hospitals intent with filter: " + filter);
        String toastMessage = "Searching for hospitals.";
        if (filter != null && filter.equals("nearby")) {
            toastMessage += " Applying 'nearby' filter.";
        }
        speaker.showToast(toastMessage);
*/
        if (speaker instanceof Context) {
            Context context = (Context) speaker; // Cast to Context
            Intent navigationIntent = null;

            speaker.showToast(response);
            // Example: Launch Google Maps for "Home" location
            // Replace with your actual home address or logic
            //TODO: Change this
            navigationIntent = new Intent(appContext, UserGetNearHealthFacilities.class);
            navigationIntent.putExtra("userId", Utils.getStoredUserId(appContext));
            navigationIntent.putExtra("filter", filter);
            context.startActivity(navigationIntent);
        } else {
            System.out.println("Speaker is not an Activity Context. Cannot start activity directly.");
            speaker.showToast("Cannot find hospitals. Speaker context is not an Activity.");
            response = "I cannot search for hospitals from this context.";
        }

        return response;
    }
}
