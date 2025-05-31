package com.example.healthhub.AI;

/**
 * Responsible for parsing the raw string output from the NLU model
 * into a structured ParsedNLUResult object.
 */
public class NLUParser {


    /**
     * Parses the NLU model's raw string output into a ParsedNLUResult object.
     * Expected format: "intent: [intent_name] | response: [response_text] | filter: [filter_value]"
     * @param nluOutputString The raw string output from the NLU model.
     * @return A ParsedNLUResult object, or a default "unknown" intent result if parsing fails.
     */
    public ParsedNLUResult parse(String nluOutputString) {
        String intent = null;
        String response = null;
        String filter = null; // Initialize filter to null

        if (nluOutputString == null || nluOutputString.trim().isEmpty()) {
            System.out.println("NLU output string is null or empty. Cannot parse.");
            // Return a default error intent if the input is bad
            return new ParsedNLUResult("unknown", "I received an empty command. Can you please speak clearly?", null);
        }

        // Split the string by " | " to get individual key-value pairs
        String[] parts = nluOutputString.split(" \\| ");

        for (String part : parts) {
            // Trim whitespace from each part
            String trimmedPart = part.trim();

            if (trimmedPart.startsWith("intent:")) {
                intent = trimmedPart.substring("intent:".length()).trim();
            } else if (trimmedPart.startsWith("response:")) {
                response = trimmedPart.substring("response:".length()).trim();
            } else if (trimmedPart.startsWith("filters:")) { // Handle the filter if it exists
                filter = trimmedPart.substring("filters:".length()).trim();
            }
            // Add more `else if` conditions here for other potential fields
            // For example:
            // else if (trimmedPart.startsWith("date:")) {
            //     date = trimmedPart.substring("date:".length()).trim();
            // }
        }

        // Basic validation: ensure intent and response were extracted
        if (intent == null || response == null) {
            System.out.println("Failed to parse NLU output. Missing intent or response. Output: " + nluOutputString);
            // Default to an 'unknown' intent with a generic error response if parsing fails partially
            return new ParsedNLUResult("unknown", "I had trouble understanding that. Could you please rephrase?", null);
        }

        return new ParsedNLUResult(intent, response, filter);
    }
}