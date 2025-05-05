package com.example.healthhub.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthhub.Utils.Utils;

import java.util.Objects;

public class Home {
    private static int _ID = 1;
    int id;
    int userID;
    String country;
    String city;
    String street;
    String number;
    String postal;

    public Home(int userID, String country, String city, String street, String number, String postal) {
        this.userID = userID;
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postal = postal;

        id = _ID;
        _ID++;

        Utils.homeDAO.addHome(this); // add to DAO
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

    public int getID() { return id; }

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

    public void updateHome(String country, String city, String street, String number, String postal) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postal = postal;

        Utils.homeDAO.updateHome(this.id, this); // Update existing home in DAO
    }
}
