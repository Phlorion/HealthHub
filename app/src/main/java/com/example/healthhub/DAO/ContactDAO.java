package com.example.healthhub.DAO;

import java.util.ArrayList;
import java.util.Objects;
public class ContactDAO {

    ArrayList<Contact> contacts;


    public ContactDAO() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact){
        contacts.add(contact);
    }

    public boolean contactExist(String phoneNum){
        return contacts.stream().anyMatch(c -> phoneNum.equals(c.phoneNum));
    }


}
