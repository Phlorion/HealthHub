package com.example.healthhub.AI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
public class RetrofitClient {
     private static final String BASE_URL = "http://192.168.1.11:5000/";

    private static RetrofitClient instance;
    private FlaskApiService flaskApiService;

    private RetrofitClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient) // Set the OkHttpClient with the interceptor
                .build();

        flaskApiService = retrofit.create(FlaskApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public FlaskApiService getFlaskApiService() {
        return flaskApiService;
    }
}