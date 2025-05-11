package com.example.healthhub.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthhub.Utils.Utils;

import java.util.Objects;

public class Contact {
    private static int _ID = 1;
    int id;
    String name;
    String phoneNum;


    public Contact(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.id = _ID;
        _ID++;

        Utils.contactDAO.addContact(this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void editName(String name){
        this.name=name;
//        Utils.contactDAO.up
    }

    public void editPhoneNum(String phoneNum){
        this.phoneNum=phoneNum;
    }
}
