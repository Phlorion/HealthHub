package com.example.healthhub.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.healthhub.DAO.ContactDAO;
import com.example.healthhub.DAO.HomeDAO;
import com.example.healthhub.DAO.MedicationDAO;
import com.example.healthhub.DAO.UserDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    /** User DAO */
    public static UserDAO userDAO = new UserDAO();

    /** Home DAO */
    public static HomeDAO homeDAO = new HomeDAO();

    /** Medication DAO */
    public static MedicationDAO medicationDAO = new MedicationDAO();

    /** Contact DAO*/
    public static ContactDAO contactDAO= new ContactDAO();

    /** Shared Preferences for accessing the application's storage */
    // constants for SharedPreferences
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    public static void saveLoginSession(Context context, int userId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }
    public static int getStoredUserId(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(KEY_USER_ID, -1);
    }
    public static void clearLoginSession(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();
    }
    /**
     * Checks if a String is a valid integer string.
     *
     * @param day The String to check.
     * @return true if the String is an integer string, false otherwise.
     * Returns false if the String is null or empty.
     */
    public static boolean isStringAnInteger(String day) {
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

    public static String getCurrentTimeString() {
//        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
//        int minute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
//        return hour * 100 + minute;
        return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
    }
    public static String getCurrentTimeAMPM() {
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        return hour < 12 ? "AM" : "PM";
    }
    public static Calendar parseTimeToCalendar(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            System.err.println("Input timeString for parseTimeToInts is null or empty.");
            return null;
        }

        try {
            // Define the format for "hh:mm a" (e.g., "02:00 am", "08:00 pm")
            // 'hh' for 12-hour format, 'mm' for minutes, 'a' for AM/PM marker
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            // Parse the string into a Date object
            Date date = sdf.parse(timeString);

            // Create a Calendar instance and set its time using the parsed Date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar;

        } catch (ParseException e) {
            System.err.println("Error parsing time string '" + timeString + "' to ints: " + e.getMessage());
            e.printStackTrace();
            return null; // Return null to indicate parsing failure
        }
    }
}
