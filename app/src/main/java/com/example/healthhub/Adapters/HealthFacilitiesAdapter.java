package com.example.healthhub.Adapters;

import android.content.Context;
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

    Context context;
    ArrayList<HealthFacilityModel> healthFacilityModels;

    public HealthFacilitiesAdapter(Context context, ArrayList<HealthFacilityModel> healthFacilityModels) {
        this.context = context;
        this.healthFacilityModels = healthFacilityModels;
    }

    @NonNull
    @Override
    public HealthFacilitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_get_near_health_facilities_rv_row, parent, false);
        return new HealthFacilitiesAdapter.ViewHolder(view);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.health_facilities_rv_row_img1);
            nameTV = itemView.findViewById(R.id.health_facilities_rv_row_tv1);
            addressTV = itemView.findViewById(R.id.health_facilities_rv_row_tv2);
            distanceTV = itemView.findViewById(R.id.health_facilities_rv_row_tv3);
        }
    }
}
