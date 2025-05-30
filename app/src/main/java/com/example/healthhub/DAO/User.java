package com.example.healthhub.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthhub.PersonalInfo;
import com.example.healthhub.Utils.Utils;

import java.util.Objects;

public class User {
    private static int _ID = 1;
    int id;
    String name;
    String email;
    String password;

    String personalInfo;



    public User(String name, String email, String password, String personalInfo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = _ID;
        _ID++;
        this.personalInfo = personalInfo;


        Utils.userDAO.addUser(this); // add user to DAO
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final User user = (User) obj;
        return Objects.equals(user.name, this.name) && Objects.equals(user.email, this.email) && Objects.equals(user.password, this.password);
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.name + "\nEmail: " + this.email + "\nPassword: " + this.password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }
}
