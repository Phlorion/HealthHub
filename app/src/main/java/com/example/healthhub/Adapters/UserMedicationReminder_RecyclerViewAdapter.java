package com.example.healthhub.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.DAO.Medication;
import com.example.healthhub.R;
import com.example.healthhub.Utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserMedicationReminder_RecyclerViewAdapter extends RecyclerView.Adapter<UserMedicationReminder_RecyclerViewAdapter.MyViewHolderUserMedication>{
    Context context;
    ArrayList<String> medicationsNames,medicationsTimes;
    // Define unique view types as constants
    private static final int VIEW_TYPE_COMPLETED = 0;
    private static final int VIEW_TYPE_UNCOMPLETED = 1;

    public UserMedicationReminder_RecyclerViewAdapter(Context context, ArrayList<String> medicationsNames, ArrayList<String> medicationsTimes) {
        this.context = context;
        this.medicationsNames = medicationsNames;
        this.medicationsTimes = medicationsTimes;
        medicationsNames.forEach(System.out::println);
        medicationsTimes.forEach(System.out::println);
    }

    @NonNull
    @Override
    public UserMedicationReminder_RecyclerViewAdapter.MyViewHolderUserMedication onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        System.out.println(viewType);

        switch (viewType) {
            case VIEW_TYPE_COMPLETED:
                view = inflater.inflate(R.layout.recycler_view_user_medication_completed, parent, false);
                break;
            case VIEW_TYPE_UNCOMPLETED:
            default: // Always have a default case, even if it's the same as one of the others
                view = inflater.inflate(R.layout.recycler_view_user_medication_uncompleted, parent, false);
                break;
        }
        return new UserMedicationReminder_RecyclerViewAdapter.MyViewHolderUserMedication(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMedicationReminder_RecyclerViewAdapter.MyViewHolderUserMedication holder, int position) {
//        Medication medication = medications.get(position);
        holder.medName.setText(medicationsNames.get(position));
        holder.medTime.setText(medicationsTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return medicationsNames.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime medicationTime = LocalTime.parse(medicationsTimes.get(position).toUpperCase(), DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()));
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(medicationTime)) {
                return VIEW_TYPE_COMPLETED;
            } else {
                return VIEW_TYPE_UNCOMPLETED;
            }
        }
        return position;
    }

    public static class MyViewHolderUserMedication extends RecyclerView.ViewHolder{
        TextView medName, medTime;
        public MyViewHolderUserMedication(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medication_name);
            medTime = itemView.findViewById(R.id.medication_time);
        }
    }
}
