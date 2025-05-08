package com.example.healthhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.DAO.Medication;

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
        holder.time.setText(medicationTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return medicationTimes.size();
    }

    public static class MyViewHolderTime extends RecyclerView.ViewHolder{
        TextView time;
        public MyViewHolderTime(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.med_row_time);
        }
    }
}
