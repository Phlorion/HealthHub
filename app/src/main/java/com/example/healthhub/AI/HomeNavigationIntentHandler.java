package com.example.healthhub.AI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.healthhub.UserGetHome;
import com.example.healthhub.Utils.Utils;

import okhttp3.internal.Util;

/**
 * Handles Home navigation intent
 */
public class HomeNavigationIntentHandler implements IntentHandler{
    @Override
    public boolean canHandle(String intentName) {
        return "navigate_home".equals(intentName);
    }

    @Override
    public String handleIntent(ParsedNLUResult parsedResult, AppActionSpeaker speaker, Context appContext) {
        String response = parsedResult.response; // Use the response from the NLU model

        // Check if the speaker can provide a Context for starting activities
        if (speaker instanceof Context) {
            Context context = (Context) speaker; // Cast to Context
            Intent navigationIntent = null;

            System.out.println("Handling navigate_home intent.");
            speaker.showToast("Navigating you home!");
            // Example: Launch Google Maps for "Home" location
            // Replace with your actual home address or logic
            //TODO: Change this
            navigationIntent = new Intent(appContext, UserGetHome.class);
            navigationIntent.putExtra("userId", Utils.getStoredUserId(appContext));
            context.startActivity(navigationIntent);
        } else {
            System.out.println("Speaker is not an Activity Context. Cannot start navigation activity directly.");
            speaker.showToast("Cannot navigate. Speaker context is not an Activity.");
            response = "I cannot start navigation from this context.";
        }
        return response; // Return the model's generated response (or a modified one)
    }
}
