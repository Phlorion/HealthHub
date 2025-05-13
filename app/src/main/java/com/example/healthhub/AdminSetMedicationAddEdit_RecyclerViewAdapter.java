package com.example.healthhub;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.DAO.Medication;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;
import java.util.ArrayList;

public class AdminSetMedicationAddEdit_RecyclerViewAdapter extends RecyclerView.Adapter<AdminSetMedicationAddEdit_RecyclerViewAdapter.MyViewHolderTime> {
    Context context;
    ArrayList<String> medicationTimes;

    public AdminSetMedicationAddEdit_RecyclerViewAdapter(Context context, ArrayList<String> medicationTimes) {
        this.context = context;
        this.medicationTimes = medicationTimes;
    }

    @NonNull
    @Override
    public AdminSetMedicationAddEdit_RecyclerViewAdapter.MyViewHolderTime onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_time_row, parent, false);
        return new AdminSetMedicationAddEdit_RecyclerViewAdapter.MyViewHolderTime(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSetMedicationAddEdit_RecyclerViewAdapter.MyViewHolderTime holder, int position) {
        int pos = position;
        holder.time.setText(medicationTimes.get(pos));
        //Extract this method to a separate class later on
        String trimmedTimeString = medicationTimes.get(pos).trim();

        String[] parts = trimmedTimeString.split(" ");
        String[] timeParts = parts[0].split(":");
        int hour12 = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        String amPm = parts[1].toUpperCase(); // Ensure AM/PM is uppercase for comparison

        int hour24 = hour12; // Initialize with 12-hour value
        if ("PM".equals(amPm)) {
            if (hour12 < 12) {
                hour24 += 12; // Convert PM to 24-hour (1 PM is 13, 2 PM is 14, etc.)
            }
        } else if ("AM".equals(amPm)) {
            if (hour12 == 12) {
                hour24 = 0; // 12 AM is 0 in 24-hour format
            }
            //  No change needed for AM hours 1-11
        }
        //Yes the method until here

        int finalHour2 = hour24;
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("edit button");
                MaterialTimePicker.Builder timePicker = new MaterialTimePicker.Builder();
                timePicker.setTimeFormat(TimeFormat.CLOCK_12H);
                LocalTime now = null;
                int currentHour = finalHour2;
                int currentMinute = minute;
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
                        medicationTimes.remove(medicationTimes.get(pos));
                        medicationTimes.add(timeString);
                        notifyDataSetChanged();
                    }
                });
                // Cancel button functionality
                picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Nothing
                    }
                });
                picker.show(((FragmentActivity) context).getSupportFragmentManager(), "Material_Time_Picker");
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicationTimes.remove(medicationTimes.get(pos));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationTimes.size();
    }

    public static class MyViewHolderTime extends RecyclerView.ViewHolder{
        TextView time;
        ImageButton editBtn, deleteBtn;
        public MyViewHolderTime(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.med_row_time);
            editBtn = itemView.findViewById(R.id.edit_button);
            deleteBtn = itemView.findViewById(R.id.delete_button);
        }
    }
}
