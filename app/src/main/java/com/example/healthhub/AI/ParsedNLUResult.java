package com.example.healthhub.AI;

import androidx.annotation.Nullable;

/**
 * A data class to hold the structured output from the NLU model.
 * Contains the identified intent, the AI's response, and any extracted entities like 'filter'.
 */
public class ParsedNLUResult {
    public final String intent;
    public final String response;
    public final String filter; // Can be null if no filter
    // Add more fields here as needed if your NLU model extracts other entities (e.g., date, location, quantity)

    /**
     * Constructor for ParsedNLUResult.
     * @param intent The identified intention of the user's command (e.g., "navigate_home", "find_hospitals").
     * @param response The AI's generated natural language response.
     * @param filter An optional filter or entity extracted from the command (e.g., "nearby", "tomorrow").
     */
    public ParsedNLUResult(String intent, String response, @Nullable String filter) {
        this.intent = intent;
        this.response = response;
        this.filter = filter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Intent: ").append(intent);
        if (filter != null && !filter.isEmpty()) {
            sb.append(", Filter: ").append(filter);
        }
        sb.append(", Response: \"").append(response).append("\"");
        return sb.toString();
    }
}
