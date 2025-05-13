package com.example.healthhub.DAO;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.healthhub.Utils.Utils;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public class Medication implements Parcelable {
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
    protected Medication(Parcel in) {
        id = in.readInt();
        userID = in.readInt();
        name = in.readString();
        // Use ParcelUtils or handle null LocalDate
        long fromDateMillis = in.readLong();
        long toDateMillis = in.readLong();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fromDate = fromDateMillis == -1 ? null : LocalDate.ofEpochDay(fromDateMillis);
            toDate = toDateMillis == -1 ? null : LocalDate.ofEpochDay(toDateMillis);
        }

        days = in.createStringArrayList();
        time = in.createStringArrayList();
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    public int getID() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public LocalDate getToDate() {
        return toDate;
    }
    public List<String> getDays() {
        return days;
    }
    public String getDaysToString() {
        if(days.size()==1 && isStringAnInteger(days.get(0))){
            return days.get(0);
        }
        StringBuilder daysString = new StringBuilder();
        for (int i = 0; i < days.size(); i++) {
            daysString.append(days.get(i));
            if(i==days.size()-1){
                return daysString.toString();
            }else{
                daysString.append(", ");
            }
        }
        return daysString.toString();
    }
    /**
     * Checks if a String is a valid integer string.
     *
     * @param day The String to check.
     * @return true if the String is an integer string, false otherwise.
     * Returns false if the String is null or empty.
     */
    private boolean isStringAnInteger(String day) {
        if (day != null && !day.isEmpty()) {
            try {
                Integer.parseInt(day);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
    public List<String> getTime() {
        return time;
    }
    public String getTimeToString() {
        StringBuilder timeString = new StringBuilder();
        for (int i = 0; i < time.size(); i++) {
            timeString.append(time.get(i));
            if(i==time.size()-1){
                return timeString.toString();
            }else{
                timeString.append(", ");
            }
        }
        return timeString.toString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userID);
        dest.writeString(name);
        // Use ParcelUtils or handle null LocalDate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dest.writeLong(fromDate == null ? -1 : fromDate.toEpochDay());
            dest.writeLong(toDate == null ? -1 : toDate.toEpochDay());
        }
        dest.writeStringList(days);
        dest.writeStringList(time);
    }
}
