package com.example.healthhub.DAO;

import java.util.ArrayList;
import java.util.Objects;

public class UserDAO {
    ArrayList<User> users;

    public UserDAO() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public boolean findUserByID(int id) {
        for (User u : users) {
            if (id == u.id) {
                return true;
            }
        }
        return false;
    }

    public boolean authUser(String email, String pass) {
        for (User u : users) {
            if (Objects.equals(u.email, email) && Objects.equals(u.password, pass)) {
                return true;
            }
        }
        return false;
    }
}
