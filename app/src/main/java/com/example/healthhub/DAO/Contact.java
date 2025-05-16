package com.example.healthhub.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthhub.Utils.Utils;
import java.util.ArrayList;
import java.util.Objects;

public class Contact {
    private static int _ID = 1;
    int id;
    String name;
    String phoneNum;
    int userID;


    public Contact(String name, String phoneNum,int userID) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.id = _ID;
        this.userID=userID;
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


    public int getID(){
        return this.id;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateContact(String name, String phoneNum){
        this.name=name;
        this.phoneNum=phoneNum;

        Utils.contactDAO.updateContact(this.id,this.name,this.phoneNum);
    }

    public int getUserID() {
        return this.userID;
    }


}

