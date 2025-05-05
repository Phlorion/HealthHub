package com.example.healthhub.DAO;

import java.util.ArrayList;

public class HomeDAO {
    ArrayList<Home> homes;

    public HomeDAO() {
        homes = new ArrayList<>();
    }

    public void addHome(Home home) {
        homes.add(home);
    }

    public void updateHome(int homeID, Home newHome) {
        for (int i = 0; i < homes.size(); i++) {
            Home h = homes.get(i);
            if (homeID == h.getID()) {
                homes.set(i, newHome); // there is no danger of setting a home with a different home id, since we are calling this method only from the update method of the Home class
                return;
            }
        }
    }

    public Home findHomeByUserID(int userId) {
        for (Home h : homes) {
            if (userId == h.userID) {
                return h;
            }
        }
        return null;
    }
}
