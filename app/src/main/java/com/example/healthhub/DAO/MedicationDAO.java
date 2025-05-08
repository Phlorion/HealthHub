package com.example.healthhub.DAO;

import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {
    ArrayList<Medication> medications;

    public MedicationDAO() {
        this.medications = new ArrayList<>();
    }
    public void addMedication(Medication medication){
        medications.add(medication);
    }
    public void updateMedication(int medicationID, Medication newMedication){
        for (int i = 0; i < medications.size(); i++) {
            Medication m = medications.get(i);
            if (medicationID == m.getID()) {
                medications.set(i, newMedication); // there is no danger of setting a home with a different home id, since we are calling this method only from the update method of the Home class
                return;
            }
        }
    }
    public void delete(Medication medication){
        medications.remove(medication);
    }
    public ArrayList<Medication> findMedicationsByUsedID(int userId){
        ArrayList<Medication> userMeds = new ArrayList<>();
        for (Medication m : medications) {
            if (userId == m.getID()) {
                userMeds.add(m);
            }
        }
        return userMeds;
    }
    public void saveMedications(ArrayList<Medication> newOrUpdatedMeds){
        for(Medication med : newOrUpdatedMeds){
            if(medications.contains(med)){//If it exists -> update it
                updateMedication(med.getID(),med);
            }else{//Else -> add it
                addMedication(med);
            }
        }
    }
}
