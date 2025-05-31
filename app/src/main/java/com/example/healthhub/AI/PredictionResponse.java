package com.example.healthhub.AI;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {
    @SerializedName("prediction") // Matches the key in your Flask JSON output
    private String prediction; // Adjust type based on your model's output (e.g., String, int, List<String>)

    @SerializedName("error") // If your Flask API returns an error message
    private String error;

    // Getters
    public String getPrediction() {
        return prediction;
    }

    public String getError() {
        return error;
    }

    // Setters (optional)
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public void setError(String error) {
        this.error = error;
    }
}
