package com.example.healthhub;

public class ContactModel {
    String contPhone;
    String contName;


    public ContactModel(String contPhone, String contName) {
        this.contPhone = contPhone;
        this.contName = contName;
    }

    public String getContPhone() {
        return contPhone;
    }

    public String getContName() {
        return contName;
    }
}



