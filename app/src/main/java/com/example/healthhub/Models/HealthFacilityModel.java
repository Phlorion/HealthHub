package com.example.healthhub.Models;

public class HealthFacilityModel {
    String name;
    String address;
    String distance;
    int image;

    public HealthFacilityModel(String name, String address, String distance, int image) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }

    public int getImage() {
        return image;
    }
}
