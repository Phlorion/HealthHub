package com.example.healthhub.Models;

public class HealthFacilityModel {
    String id;
    String name;
    String address;
    String distance;
    int image;

    public HealthFacilityModel(String id, String name, String address, String distance, int image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.image = image;
    }

    public String getId() {
        return id;
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
