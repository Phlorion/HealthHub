package com.example.healthhub.AI;

import com.google.gson.annotations.SerializedName;

public class PredictionRequest {
    @SerializedName("text") // Matches the key in your Flask JSON
    private String text;

    // Constructor
    public PredictionRequest(String text) {
        this.text = text;
    }

    // Getter (optional, but good practice)
    public String getText() {
        return text;
    }

    // Setter (optional)
    public void setText(String text) {
        this.text = text;
    }
}