package com.example.healthhub.DAO;

import com.example.healthhub.Utils.Utils;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Medication {
    private static int _ID = 1;
    int id;
    int userID;
    String name;
    LocalDate fromDate;
    LocalDate toDate;
    List<String> days;
    List<String> time;

    public Medication(int userID, String name, LocalDate fromDate, LocalDate toDate, List<String> days, List<String> time) {
        this.userID = userID;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.days = days;
        this.time = time;

        this.id = _ID;
        _ID++;

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

    public List<String> getDays() {
        return days;
    }

    public List<String> getTime() {
        return time;
    }
    public void saveMedicationToDAO() {
        Utils.medicationDAO.addMedication(this); // add to DAO
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return id == that.id && userID == that.userID && Objects.equals(name, that.name) && Objects.equals(days, that.days) && Objects.equals(time, that.time);
    }

    @NonNull
    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", userID=" + userID +
                ", name='" + name + '\'' +
                ", days='" + days + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
