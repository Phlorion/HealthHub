package com.example.healthhub.DAO;

import com.example.healthhub.Utils.Utils;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import java.util.Objects;

public class Medication {
    private static int _ID = 1;
    int id;
    int userID;
    String name;
    String date;
    String time;

    public Medication(int userID, String name, String date, String time) {
        this.userID = userID;
        this.name = name;
        this.date = date;
        this.time = time;

        this.id = _ID;
        _ID++;

        Utils.medicationDAO.addMedication(this); // add to DAO
    }

    public int getID() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return id == that.id && userID == that.userID && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @NonNull
    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", userID=" + userID +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
