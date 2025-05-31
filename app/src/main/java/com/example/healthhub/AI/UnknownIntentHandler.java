package com.example.healthhub.AI;

import android.content.Context;

public class UnknownIntentHandler implements IntentHandler {
    @Override
    public boolean canHandle(String intentName) {
        return "unknown".equals(intentName) || true;
    }

    @Override
    public String handleIntent(ParsedNLUResult parsedResult, AppActionSpeaker speaker, Context appContext) {
        System.out.println("Handling unknown intent: " + parsedResult.intent);

        // Use the response from the NLU model if it provided one for "unknown"
        // Otherwise, provide a generic fallback.
        String response = parsedResult.response != null && !parsedResult.response.isEmpty() ?
                parsedResult.response : "I'm sorry, I didn't understand that command. Can you please try again?";

        speaker.showToast("Couldn't understand command.");
        return response;
    }
}
