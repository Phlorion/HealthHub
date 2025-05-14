package com.example.healthhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.Models.HealthFacilityModel;
import com.example.healthhub.R;

import java.util.ArrayList;

public class HealthFacilitiesAdapter extends RecyclerView.Adapter<HealthFacilitiesAdapter.ViewHolder> {

    private final HealthFacilityRVInterface healthFacilityRVInterface;
    Context context;
    ArrayList<HealthFacilityModel> healthFacilityModels;

    public HealthFacilitiesAdapter(Context context, ArrayList<HealthFacilityModel> healthFacilityModels, HealthFacilityRVInterface healthFacilityRVInterface) {
        this.healthFacilityRVInterface = healthFacilityRVInterface;
        this.context = context;
        this.healthFacilityModels = healthFacilityModels;
    }

    @NonNull
    @Override
    public HealthFacilitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_get_near_health_facilities_rv_row, parent, false);
        return new HealthFacilitiesAdapter.ViewHolder(view, healthFacilityRVInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthFacilitiesAdapter.ViewHolder holder, int position) {
        holder.nameTV.setText(healthFacilityModels.get(position).getName());
        holder.addressTV.setText(healthFacilityModels.get(position).getAddress());
        holder.distanceTV.setText(healthFacilityModels.get(position).getDistance());
        holder.img.setImageResource(healthFacilityModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return healthFacilityModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nameTV, addressTV, distanceTV;

        public ViewHolder(@NonNull View itemView, HealthFacilityRVInterface healthFacilityRVInterface) {
            super(itemView);

            img = itemView.findViewById(R.id.health_facilities_rv_row_img1);
            nameTV = itemView.findViewById(R.id.health_facilities_rv_row_tv1);
            addressTV = itemView.findViewById(R.id.health_facilities_rv_row_tv2);
            distanceTV = itemView.findViewById(R.id.health_facilities_rv_row_tv3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (healthFacilityRVInterface != null) {
                        int pos = getAbsoluteAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            healthFacilityRVInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
