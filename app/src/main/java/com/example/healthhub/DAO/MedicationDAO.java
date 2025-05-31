package com.example.healthhub.DAO;

import android.os.Build;

import com.example.healthhub.Utils.Utils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
            System.out.println("Medication "+m.getID()+" belongs to user "+m.getUserID());
            if (userId == m.getUserID()) {
                System.out.println("userId: "+userId+" m.getUserID(): "+m.getUserID());
                userMeds.add(m);
            }else{
                System.out.println("userId: "+userId+" m.getUserID(): "+m.getUserID());
            }
        }
        return userMeds;
    }

    public ArrayList<Medication> findMedicationsByUserIDAndDate(int userId, LocalDate date){
        ArrayList<Medication> datesMedications = new ArrayList<>();
        //Get all medications by use id
        ArrayList<Medication> userMedications = findMedicationsByUsedID(userId);
        //For each medication
        for (Medication medication : userMedications) {
            //Get Valid dates
            ArrayList<LocalDate> medicationValidDates = getMedicationValidDates(medication);
            //If date in valid dates -> add medication to list
            if(medicationValidDates.contains(date)){
                datesMedications.add(medication);
            }
        }
        return datesMedications;
    }

    private ArrayList<LocalDate> getMedicationValidDates(Medication medication){
        ArrayList<LocalDate> medicationValidDates = new ArrayList<>();
        LocalDate fromDate = medication.getFromDate();
        LocalDate toDate = medication.getToDate();
        List<String> days = medication.getDays();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

            if(days.size()==1 && Utils.isStringAnInteger(days.get(0))){//if ever X day(S)
                medicationValidDates.add(fromDate);
                for (int i = 1; i <= daysBetween; i++) {
                    if(i%Integer.parseInt(days.get(0))==0){
                        medicationValidDates.add(fromDate.plusDays(Long.parseLong(String.valueOf(i))));
                    }else{
                        System.out.println("Date not added"+i);
                    }
                }
//                LocalDate testDate = fromDate.plusDays(Long.parseLong(days.get(0)));
            }else{//if specific days
                for (int i = 0; i <= daysBetween; i++) {
                    //If date is in days list -> add to list
                    LocalDate testDate = fromDate.plusDays(Long.parseLong(String.valueOf(i)));
                    String dayOfWeek = testDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
//                    String dayOfWeek = testDate.getDayOfWeek().toString().toLowerCase(Locale.ROOT);
                    if(days.contains(dayOfWeek)){
                        medicationValidDates.add(testDate);
                    }else{
                        System.out.println("Date not added"+dayOfWeek);
                    }
                }

            }
        }
        return medicationValidDates;
    }

    public ArrayList<Calendar> getMedicationValidDateAndTimes(Medication medication){
        ArrayList<Calendar> medicationValidDatesAndTimes = new ArrayList<>();
        LocalDate fromDate = medication.getFromDate();
        LocalDate toDate = medication.getToDate();
        List<String> days = medication.getDays();
        List<String> times = medication.getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

            if(days.size()==1 && Utils.isStringAnInteger(days.get(0))){//if ever X day(S)
                for (int i = 0; i <= daysBetween; i++) {
                    if(i%Integer.parseInt(days.get(0))==0){
                        LocalDate currentDate = fromDate.plusDays(i);
                        for (String time : times) {
                            Calendar calendar = Utils.parseTimeToCalendar(time);
                            calendar.set(Calendar.YEAR, currentDate.getYear());
                            calendar.set(Calendar.MONDAY,currentDate.getMonthValue()-1);
                            calendar.set(Calendar.DAY_OF_MONTH,currentDate.getDayOfMonth());
                            medicationValidDatesAndTimes.add(calendar);
                        }
                    }else{
                        System.out.println("Notification for date not added "+i);
                    }
                }
            }else{//if specific days
                for (int i = 0; i <= daysBetween; i++) {
                    //If date is in days list -> add to list
                    LocalDate testDate = fromDate.plusDays(Long.parseLong(String.valueOf(i)));
                    String dayOfWeek = testDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
                    if(days.contains(dayOfWeek)){
                        for (String time : times) {
                            Calendar calendar = Utils.parseTimeToCalendar(time);
                            calendar.set(Calendar.YEAR, testDate.getYear());
                            calendar.set(Calendar.MONDAY,testDate.getMonthValue()-1);
                            calendar.set(Calendar.DAY_OF_MONTH,testDate.getDayOfMonth());
                            medicationValidDatesAndTimes.add(calendar);
                        }
                    }else{
                        System.out.println("Notification for date not added "+dayOfWeek);
                    }
                }

            }
        }
        return medicationValidDatesAndTimes;
    }

    /**
     * Save all medications to the database - if they exist -> update them, else -> add them
     * */
    public void saveMedications(ArrayList<Medication> newOrUpdatedMeds){
        for (Medication newMed : newOrUpdatedMeds) {
            boolean updated = false;

            for (int i = 0; i < medications.size(); i++) {
                Medication existingMed = medications.get(i);
                if (existingMed.getID() == (newMed.getID())) {
                    updateMedication(newMed.getID(), newMed);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                addMedication(newMed);
            }
        }
    }
    public void printMedications(){
        medications.forEach(System.out::println);
    }
}
