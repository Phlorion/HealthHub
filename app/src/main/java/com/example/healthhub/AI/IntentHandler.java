package com.example.healthhub.AI;

import android.content.Context;

/**
 * Interface for classes that are responsible for handling specific NLU intents.
 * This promotes modularity and keeps intent-specific logic separate.
 */
public interface IntentHandler {
    /**
     * Checks if this handler can process a given intent name.
     * @param intentName The name of the intent (e.g., "navigate_home").
     * @return true if this handler can process the intent, false otherwise.
     */
    boolean canHandle(String intentName);

    /**
     * Handles the given parsed NLU result and performs the associated action.
     * @param parsedResult The structured result from the NLU model.
     * @param speaker An AppActionSpeaker instance to provide UI feedback (toasts, status updates) and speak responses.
     * @return The response string to be spoken or displayed to the user.
     */
    String handleIntent(ParsedNLUResult parsedResult, AppActionSpeaker speaker, Context appContext);
}