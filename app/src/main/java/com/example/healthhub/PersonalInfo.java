package com.example.healthhub;

public class PersonalInfo {
    private static String personalInfo = "";

    public static String getPersonalInfo() {
        return personalInfo;
    }

    public static void setPersonalInfo(String info) {
        personalInfo = info;
    }
}