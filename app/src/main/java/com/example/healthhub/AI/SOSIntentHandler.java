package com.example.healthhub.AI;

import android.content.Context;
import android.content.Intent;

import com.example.healthhub.UserGetNearHealthFacilities;
import com.example.healthhub.UserSos;
import com.example.healthhub.Utils.Utils;

public class SOSIntentHandler implements IntentHandler{
    @Override
    public boolean canHandle(String intentName) {
        return  intentName.toLowerCase().contains(("sos")) ||
                intentName.toLowerCase().contains(("help")) ||
                intentName.toLowerCase().contains(("emergency")) ||
                intentName.toLowerCase().contains(("ambulance"));
    }

    @Override
    public String handleIntent(ParsedNLUResult parsedResult, AppActionSpeaker speaker, Context appContext) {
        String response = parsedResult.response;
        String filter = parsedResult.filter; // e.g., "nearby"

        if (speaker instanceof Context) {
            Context context = (Context) speaker; // Cast to Context
            Intent intent = null;

            speaker.showToast(response);
            // Example: Launch Google Maps for "Home" location
            // Replace with your actual home address or logic
            //TODO: Change this
            intent = new Intent(appContext, UserSos.class);
            intent.putExtra("userId", Utils.getStoredUserId(appContext));
            intent.putExtra("filter", filter);
            context.startActivity(intent);
        } else {
            System.out.println("Speaker is not an Activity Context. Cannot start activity directly.");
            speaker.showToast("Cannot find hospitals. Speaker context is not an Activity.");
            response = "I cannot help from this context.";
        }

        return response;
    }
}
