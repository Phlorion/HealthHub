package com.example.healthhub.Models;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;

import java.time.LocalDate;
import java.time.ZoneOffset;

@SuppressLint("ParcelCreator")
public class MedicationDateValidator implements CalendarConstraints.DateValidator {
    private Long minDate;
    private Long maxDate;
    public MedicationDateValidator(Long minDate, Long maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }


    @Override
    public boolean isValid(long date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // if before today
            if(minDate!=null && date < minDate){
//            if(date < LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()){
                System.out.println("1");
                return false;
            }
            // if after max date
            if(maxDate != null && date > maxDate){
                System.out.println("2");
                return false;
            }
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
