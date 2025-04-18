package com.example.healthhub.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Home {
    private static int _ID = 1;
    int id;
    String country;
    String city;
    String street;
    String number;
    String postal;

    public Home() { // this constructor is used only when a user is created, to create his home id
        this.id = _ID;
        _ID++;
    }

    public Home(String country, String city, String street, String number, String postal) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postal = postal;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Home home = (Home) obj;
        return Objects.equals(this.country, home.country) && Objects.equals(this.city, home.city) && Objects.equals(this.street, home.street)
                && Objects.equals(this.number, home.number) && Objects.equals(this.postal, home.postal);
    }

    @NonNull
    @Override
    public String toString() {
        return "Country: " + country +"\nCity: " + city + "\nStreet: " + street + "\nNumber: " + number + "\nPostal Code: " + postal;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostal() {
        return postal;
    }

    public boolean registered() {
        return country != null;
    }

    public void updateHome(Home home) {
        this.country = home.country;
        this.city = home.city;
        this.street = home.street;
        this.number = home.number;
        this.postal = home.postal;
    }
}
