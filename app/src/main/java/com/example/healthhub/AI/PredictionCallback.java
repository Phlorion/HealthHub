package com.example.healthhub.AI;

public interface PredictionCallback {
    void onPredictionSuccess(String prediction);
    void onPredictionFailure(String errorMessage);
}