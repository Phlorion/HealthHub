package com.example.healthhub.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class User {
    String name;
    String email;
    String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
}
