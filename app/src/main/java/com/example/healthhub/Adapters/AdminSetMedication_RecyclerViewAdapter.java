package com.example.healthhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.AdminSetMedicationAddEdit;
import com.example.healthhub.DAO.Medication;
import com.example.healthhub.R;
import com.example.healthhub.Utils.Utils;

import java.util.ArrayList;

public class AdminSetMedication_RecyclerViewAdapter extends RecyclerView.Adapter<AdminSetMedication_RecyclerViewAdapter.MyViewHolderMedication> {
    private final AdminSetMedication_RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Medication> medications;
    public AdminSetMedication_RecyclerViewAdapter(Context context, ArrayList<Medication> medications, AdminSetMedication_RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.medications = medications;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public AdminSetMedication_RecyclerViewAdapter.MyViewHolderMedication onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_medication_row, parent, false);
        return new AdminSetMedication_RecyclerViewAdapter.MyViewHolderMedication(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSetMedication_RecyclerViewAdapter.MyViewHolderMedication holder,  int position) {
        Medication medication = medications.get(position);
        holder.medName.setText(medication.getName());
        //From - to DATE
        holder.medDays.setText(medication.getDaysToString());
        holder.medTime.setText(medication.getTimeToString());

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("edit button");
                Context context = v.getContext();
                Intent intent = new Intent(context, AdminSetMedicationAddEdit.class);
                intent.putExtra("medication",medication);
                context.startActivity(intent);
            }
        });
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("delete button");
                Utils.medicationDAO.delete(medication);
                medications.remove(medication);
                notifyItemRemoved(holder.getAdapterPosition());
                Toast.makeText(v.getContext(), "Item removed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public static class MyViewHolderMedication extends RecyclerView.ViewHolder{
        TextView medName, medDays, medTime;
        ImageButton editBtn, delBtn;
        public MyViewHolderMedication(@NonNull View itemView,AdminSetMedication_RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            medName = itemView.findViewById(R.id.medication_name);
            medDays = itemView.findViewById(R.id.medication_days);
            medTime = itemView.findViewById(R.id.medication_time);
            editBtn = itemView.findViewById(R.id.edit_button);
            delBtn = itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos!= RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
