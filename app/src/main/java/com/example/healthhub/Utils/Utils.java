package com.example.healthhub.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.healthhub.DAO.HomeDAO;
import com.example.healthhub.DAO.UserDAO;

public class Utils {
    /** User DAO */
    public static UserDAO userDAO = new UserDAO();

    /** Home DAO */
    public static HomeDAO homeDAO = new HomeDAO();

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
}
