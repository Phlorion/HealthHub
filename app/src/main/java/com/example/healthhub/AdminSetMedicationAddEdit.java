package com.example.healthhub;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.example.healthhub.DAO.Medication;
import com.example.healthhub.Utils.Utils;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;
import java.util.ArrayList;

public class AdminSetMedicationAddEdit extends AppCompatActivity {

    Button backBtn, saveBtn;
    TextView displayedDays;
    EditText nameEditText, quantityEditText, fromEditText, toEditText;
    ImageButton incrementImgBtn, decrementImgBtn, addTimeSlotImgBtn;
    CheckBox monCheckBox, tueCheckBox, wedCheckBox, thuCheckBox, friCheckBox, satCheckBox, sunCheckBox;
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

        String time = getIntent().getStringExtra("time");
        RecyclerView recyclerView = findViewById(R.id.time_slots_recycler_view);
        ArrayList<String> timeSlots = new ArrayList<>();
        timeSlots.add(time);
        AdminSetMedicationAddEdit_RecyclerViewAdapter adapter = new AdminSetMedicationAddEdit_RecyclerViewAdapter(this, timeSlots);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.back_button);
        saveBtn = findViewById(R.id.save_button);
        displayedDays = findViewById(R.id.number_display);
        nameEditText = findViewById(R.id.name_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        fromEditText = findViewById(R.id.from_edit_text);
        toEditText = findViewById(R.id.to_edit_text);
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
                    System.out.println("katse kala");
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
    }
    private boolean saveMedications(){
        String displayedDays = getDisplayedDays(), name = getName(), quantity = getQuantity(), from = getFromDate(), to = getToDate();
        if(displayedDays.isEmpty() || name.isEmpty() || quantity.isEmpty() || from.isEmpty() || to.isEmpty()){
            Toast.makeText(this, "All fields must be completed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        System.out.println(this.isMonChecked());
        System.out.println(this.isTueChecked());
        System.out.println(this.isWedChecked());
        System.out.println(this.isThuChecked());
        System.out.println(this.isFriChecked());
        System.out.println(this.isSatChecked());
        System.out.println(this.isSunChecked());
//        new Medication(Utils.getStoredUserId(getApplicationContext()),"test","test","test");
//        Utils.medicationDAO.addMedication(new Medication(Utils.getStoredUserId(getApplicationContext()),"test","test","test"));
        return true;
    }
    private void incrementDisplayedDays() {
        int displayedDays = Integer.parseInt(getDisplayedDays());
        if (displayedDays < 7) {
            displayedDays++;
            this.displayedDays.setText(String.valueOf(displayedDays));
        }
    }
    private void decrementDisplayedDays() {
        int displayedDays = Integer.parseInt(getDisplayedDays());
        if (displayedDays > 0) {
            displayedDays--;
            this.displayedDays.setText(String.valueOf(displayedDays));
        }
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
//        timePicker.setTheme(R.style.MyTimePickerDialog);
        // Show the MaterialTimePicker
        MaterialTimePicker picker = timePicker.build();
        picker.show(getSupportFragmentManager(), "Material_Time_Picker");
    }
    private String getDisplayedDays(){
        return displayedDays.getText().toString();
    }
    private String getName(){
        return nameEditText.getText().toString();
    }
    private String getQuantity(){
        return quantityEditText.getText().toString();
    }
    private String getFromDate(){
        return fromEditText.getText().toString();
    }
    private String getToDate(){
        return toEditText.getText().toString();
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
