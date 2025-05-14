package com.example.healthhub.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public class HealthFacilityModel {
    String id;
    String name;
    String address;
    String distance;
    String primaryType;
    LatLng location;
    Place.BusinessStatus businessStatus;
    int image;

    public HealthFacilityModel(String id, String name, String address, String distance, String primaryType, LatLng location, Place.BusinessStatus businessStatus, int image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.primaryType = primaryType;
        this.location = location;
        this.businessStatus = businessStatus;
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

    public String getPrimaryType() {
        return primaryType;
    }

    public LatLng getLocation() {
        return location;
    }

    public Place.BusinessStatus getBusinessStatus() {
        return businessStatus;
    }
}
