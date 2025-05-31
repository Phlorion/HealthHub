package com.example.healthhub.AI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FlaskApiService {
    @POST("/predict")
    Call<PredictionResponse> getPrediction(@Body PredictionRequest request);
}