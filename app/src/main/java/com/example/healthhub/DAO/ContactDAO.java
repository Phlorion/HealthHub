package com.example.healthhub.DAO;

import android.content.Intent;

import com.example.healthhub.Models.ContactModel;
import com.example.healthhub.Utils.Utils;

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


    public void removeContact(Contact contact) {
        for (Contact c : contacts) {
            if (c.getPhoneNum().equals(contact.getPhoneNum())) {
                contacts.remove(c);
                return;
            }
        }
    }


    private Contact convertModelToContact(ContactModel model) {
        for (Contact c : Utils.contactDAO.getContacts()) {
            if (c.getPhoneNum().equals(model.getContPhone()) &&
                    c.getName().equals(model.getContName())) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void updateContact(int contactID, String name, String phoneNum){
       for (int i = 0; i < contacts.size(); i++) {
           Contact c = contacts.get(i);
           if (contactID == c.getID()) {
               c.setName(name);
               c.setPhoneNum(phoneNum);
               return;
           }
   }
}
}
