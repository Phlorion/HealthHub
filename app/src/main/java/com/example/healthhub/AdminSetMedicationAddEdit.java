package com.example.healthhub;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.Adapters.AdminSetMedicationAddEdit_RecyclerViewAdapter;
import com.example.healthhub.DAO.Medication;
import com.example.healthhub.Models.MedicationDateValidator;
import com.example.healthhub.Utils.Utils;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdminSetMedicationAddEdit extends AppCompatActivity {
    Medication currentMedication;
    ArrayList<String> timeSlots;
    AdminSetMedicationAddEdit_RecyclerViewAdapter adapter;
    Button backBtn, saveBtn;
    TextView displayedDays, fromDatePicker, toDatePicker;
    EditText nameEditText, quantityEditText;
    ImageButton incrementImgBtn, decrementImgBtn, addTimeSlotImgBtn;
    CheckBox monCheckBox, tueCheckBox, wedCheckBox, thuCheckBox, friCheckBox, satCheckBox, sunCheckBox;
    private List<CheckBox> dayOfWeekCheckBoxes;
    RecyclerView recyclerView;
    private Long fromEpoch = null;
    private Long toEpoch   = null;
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_set_medication_add_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.back_button);
        saveBtn = findViewById(R.id.save_button);
        displayedDays = findViewById(R.id.number_display);
        nameEditText = findViewById(R.id.name_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        fromDatePicker = findViewById(R.id.from_date_picker);
        toDatePicker = findViewById(R.id.to_date_picker);
        incrementImgBtn = findViewById(R.id.increment_button);
        decrementImgBtn = findViewById(R.id.decrement_button);
        addTimeSlotImgBtn = findViewById(R.id.add_time_button);
        monCheckBox = findViewById(R.id.monday_checkbox);
        tueCheckBox = findViewById(R.id.tuesday_checkbox);
        wedCheckBox = findViewById(R.id.wednesday_checkbox);
        thuCheckBox = findViewById(R.id.thursday_checkbox);
        friCheckBox = findViewById(R.id.friday_checkbox);
        satCheckBox = findViewById(R.id.saturday_checkbox);
        sunCheckBox = findViewById(R.id.sunday_checkbox);
        recyclerView = findViewById(R.id.time_slots_recycler_view);
        dayOfWeekCheckBoxes = new ArrayList<>();
        dayOfWeekCheckBoxes.add(monCheckBox);
        dayOfWeekCheckBoxes.add(tueCheckBox);
        dayOfWeekCheckBoxes.add(wedCheckBox);
        dayOfWeekCheckBoxes.add(thuCheckBox);
        dayOfWeekCheckBoxes.add(friCheckBox);
        dayOfWeekCheckBoxes.add(satCheckBox);
        dayOfWeekCheckBoxes.add(sunCheckBox);
        setupDayOfWeekCheckBoxListeners();

        currentMedication = getIntent().getParcelableExtra("medication");
        timeSlots = new ArrayList<>();
        if (currentMedication != null) {
            timeSlots.addAll(currentMedication.getTime());
            nameEditText.setText(currentMedication.getName());
            quantityEditText.setText(currentMedication.getQuantity());
            if (currentMedication.getFromDate() != null) {
                fromDatePicker.setText(currentMedication.getFromDateAsString());
            }
            if (currentMedication.getToDate() != null) {
                toDatePicker.setText(currentMedication.getToDateAsString());
            }
            // Set Days
            setDays(currentMedication.getDays());
        }
        adapter = new AdminSetMedicationAddEdit_RecyclerViewAdapter(this, timeSlots);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveMedications()){
                    finish();
                }
            }
        });
        incrementImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDisplayedDays();
            }
        });
        decrementImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementDisplayedDays();
            }
        });
        addTimeSlotImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimeSlot();
            }
        });
        fromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromPicker();
            }
        });
        toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToPicker();
            }
        });
    }
    private boolean saveMedications(){
        // Firstly make sure the user has entered all the fields
        String  name = getName(), quantity = getQuantity(), from = getFromDate(), to = getToDate(),displayedDays = getDisplayedDays();
        boolean daysSelected = false;
        if(isMonChecked() || isTueChecked() || isWedChecked() || isThuChecked() || isFriChecked() || isSatChecked() || isSunChecked()||displayedDays.compareTo("1") >= 0){
            daysSelected = true;
        }else{
            Toast.makeText(this, "At least one day must be selected.", Toast.LENGTH_SHORT).show();
        }
        if(name.isEmpty() || quantity.isEmpty() || from.isEmpty() || to.isEmpty()|| !daysSelected || timeSlots.isEmpty()){
            Toast.makeText(this, "All fields must be completed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Now save the medication
        if(currentMedication == null){
            currentMedication = new Medication(Utils.getStoredUserId(getApplicationContext()),name,quantity,transformStringToDate(from),transformStringToDate(to),getDaysList(),getTimeSlots());
        }else{//Update medication
            currentMedication.setName(name);
            currentMedication.setQuantity(quantity);
            currentMedication.setFromDate(transformStringToDate(from));
            currentMedication.setToDate(transformStringToDate(to));
            currentMedication.setDays(getDaysList());
            currentMedication.setTime(getTimeSlots());
        }
        Objects.requireNonNull(currentMedication).saveMedicationToDAO();
        ArrayList<Calendar> medicationValidDatesAndTimes = Utils.medicationDAO.getMedicationValidDateAndTimes(currentMedication);
        medicationValidDatesAndTimes.forEach(calendar -> UserMedicationScheduler.scheduleAlarm(this,calendar.getTimeInMillis()));
        System.out.println("Here here here: "+currentMedication);
        return true;
    }
    private void showFromPicker() {

        // Build calendar constraints
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        // Build custom validator
        MedicationDateValidator medicationDateValidator = new MedicationDateValidator(null,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (fromEpoch != null) {
                medicationDateValidator = new MedicationDateValidator(Math.min(fromEpoch, LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()), toEpoch);
            } else {
                medicationDateValidator = new MedicationDateValidator(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), toEpoch);
            }
        }
        constraintsBuilder.setValidator(medicationDateValidator);

        // Build Material Picker
        MaterialDatePicker.Builder<Long> fromPicker = MaterialDatePicker.Builder.datePicker();
        fromPicker.setTitleText("Select initial date");
        fromPicker.setCalendarConstraints(constraintsBuilder.build());

        // Pre-select previously picked FROM date, if any
        // Default selection: today
        fromPicker.setSelection(Objects.requireNonNullElseGet(fromEpoch, MaterialDatePicker::todayInUtcMilliseconds));

        MaterialDatePicker<Long> fromMaterialDatePicker = fromPicker.build();
        fromMaterialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromMaterialDatePicker.dismiss();
            }
        });
        fromMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                fromEpoch = selection;
                fromDatePicker.setText(fmt.format(selection));
            }
        });
        fromMaterialDatePicker.show(getSupportFragmentManager(), "FROM_PICKER");
    }
    private void showToPicker() {
        // Build calendar constraints
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        // Build custom validator
        MedicationDateValidator medicationDateValidator = new MedicationDateValidator(null,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (fromEpoch != null) {
                medicationDateValidator = new MedicationDateValidator(Math.max(fromEpoch, LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()), toEpoch);
            } else {
                medicationDateValidator = new MedicationDateValidator(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), toEpoch);
            }
        }
        constraintsBuilder.setValidator(medicationDateValidator);

        // Build Material Picker
        MaterialDatePicker.Builder<Long> toPicker = MaterialDatePicker.Builder.datePicker();
        toPicker.setTitleText("Select final date");
        toPicker.setCalendarConstraints(constraintsBuilder.build());
        // pre-select previous value if exists
        if (toEpoch != null) {
            toPicker.setSelection(toEpoch);
        }

        MaterialDatePicker<Long> toMaterialdatePicker = toPicker.build();

        toMaterialdatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMaterialdatePicker.dismiss();
            }
        });
        toMaterialdatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                toEpoch = selection;
                toDatePicker.setText(fmt.format(selection));
            }
        });
        toMaterialdatePicker.show(getSupportFragmentManager(), "TO_PICKER");
    }
    private void incrementDisplayedDays() {
        int displayedDays = Integer.parseInt(getDisplayedDays());
        if (displayedDays < 7) {
            displayedDays++;
            this.displayedDays.setText(String.valueOf(displayedDays));
        }
        //Uncheck all checkboxes
        setAllCheckboxes(false);
    }
    private void decrementDisplayedDays() {
        int displayedDays = Integer.parseInt(getDisplayedDays());
        if (displayedDays > 0) {
            displayedDays--;
            this.displayedDays.setText(String.valueOf(displayedDays));
        }
        //Uncheck all checkboxes
        setAllCheckboxes(false);
    }
    private void setAllCheckboxes(boolean isChecked){
        monCheckBox.setChecked(isChecked);
        tueCheckBox.setChecked(isChecked);
        wedCheckBox.setChecked(isChecked);
        thuCheckBox.setChecked(isChecked);
        friCheckBox.setChecked(isChecked);
        satCheckBox.setChecked(isChecked);
        sunCheckBox.setChecked(isChecked);
    }
    private void setupDayOfWeekCheckBoxListeners() {
        for (CheckBox checkBox : dayOfWeekCheckBoxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Logic: If a checkbox is checked, set the day number to 0
                    if (isChecked) {
                        setDayNumberToZero();
                    }
                    // Note: We don't need to uncheck other checkboxes here.
                    // The user can select multiple days of the week.
                    // The unchecking happens when the day number counter is used.
                }
            });
        }
    }
    private void setDayNumberToZero() {
        displayedDays.setText("0");
    }
    private void addTimeSlot() {
        // Set-up the MaterialTimePicker
        MaterialTimePicker.Builder timePicker = new MaterialTimePicker.Builder();
        timePicker.setTimeFormat(TimeFormat.CLOCK_12H);
        LocalTime now = null;
        int currentHour = 12;
        int currentMinute = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalTime.now();
            currentHour = now.getHour();
            currentMinute = now.getMinute();
        }
        timePicker.setHour(currentHour);
        timePicker.setMinute(currentMinute);
        timePicker.setTitleText("Select Medication Time");
        timePicker.setPositiveButtonText("Save");
        timePicker.setNegativeButtonText("Cancel");
        // Show the MaterialTimePicker
        MaterialTimePicker picker = timePicker.build();
        // Save button functionality
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return the sting we want to add to the recycler view or a LocalTime object
                int hour = picker.getHour();
                int minute = picker.getMinute();
                String amPm = (hour < 12) ? "am" : "pm";
                // Convert 24-hour format to 12-hour format
                if (hour > 12) {
                    hour -= 12;
                } else if (hour == 0) {
                    hour = 12; // Midnight
                }
                String timeString = String.format("%02d:%02d %s", hour, minute, amPm);
                //call private method to check if the time is already in the list
                addToTimeSlotsCollection(timeString);
            }
        });
        // Cancel button functionality
        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nothing
            }
        });
        picker.show(getSupportFragmentManager(), "Material_Time_Picker");
    }
    private void addToTimeSlotsCollection(String timeString){
        if(!timeSlots.contains(timeString)){
            timeSlots.add(timeString);
            adapter.notifyDataSetChanged(); // Notify the adapter
        }else{
            Toast.makeText(this, "Time slot already exists.", Toast.LENGTH_SHORT).show();
        }
    }
    private LocalDate transformStringToDate(String dateString){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                return LocalDate.parse(dateString,formatter);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid date format.", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
    private List<String> getDaysList(){
        List<String> days = new ArrayList<>();
        if(isMonChecked()){
            days.add("Mon");
        }
        if(isTueChecked()){
            days.add("Tue");
        }
        if(isWedChecked()){
            days.add("Wed");
        }
        if(isThuChecked()){
            days.add("Thu");
        }
        if(isFriChecked()){
            days.add("Fri");
        }
        if(isSatChecked()){
            days.add("Sat");
        }
        if(isSunChecked()){
            days.add("Sun");
        }
        if(days.isEmpty()){
            days.add(getDisplayedDays());
        }
        return days;
    }
    private void setDays(List<String> days){
        if(days.size()==1 && Utils.isStringAnInteger(days.get(0))){
            displayedDays.setText(days.get(0));
        }else{
            for (String day : days) {
                switch (day) {
                    case "Mon":
                        monCheckBox.setChecked(true);
                        break;
                    case "Tue":
                        tueCheckBox.setChecked(true);
                        break;
                    case "Wed":
                        wedCheckBox.setChecked(true);
                        break;
                    case "Thu":
                        thuCheckBox.setChecked(true);
                        break;
                    case "Fri":
                        friCheckBox.setChecked(true);
                        break;
                    case "San":
                        thuCheckBox.setChecked(true);
                        break;
                    case "Sun":
                        friCheckBox.setChecked(true);
                        break;
                    default:
                        System.out.println("Invalid day: "+day);
                        break;
                }
            }
        }
    }
    private ArrayList<String> getTimeSlots() {
        return timeSlots;
    }
    private String getDisplayedDays(){
        return displayedDays.getText().toString();
    }
    private String getName(){
        return nameEditText.getText().toString().trim();
    }
    private String getQuantity(){
        return quantityEditText.getText().toString().trim();
    }
    private String getFromDate(){
        return fromDatePicker.getText().toString();
    }
    private String getToDate(){
        return toDatePicker.getText().toString();
    }
    private boolean isMonChecked(){
        return monCheckBox.isChecked();
    }
    private boolean isTueChecked(){
        return tueCheckBox.isChecked();
    }
    private boolean isWedChecked(){
        return wedCheckBox.isChecked();
    }
    private boolean isThuChecked(){
        return thuCheckBox.isChecked();
    }
    private boolean isFriChecked(){
        return friCheckBox.isChecked();
    }
    private boolean isSatChecked(){
        return satCheckBox.isChecked();
    }
    private boolean isSunChecked(){
        return sunCheckBox.isChecked();
    }
}
