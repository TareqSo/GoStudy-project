package com.example.gostudy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> materials;

    // Update the constructor to accept a list of strings, not buttons
    public RecyclerViewAdapter(List<String> materials) {
        this.materials = materials;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String material = materials.get(position);
        holder.bind(material);
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    // ViewHolder should contain a Button, and the bind method should accept a String
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Make sure R.id.button is the ID of the button in your button_layout.xml
            button = itemView.findViewById(R.id.recycler_button);
        }

        public void bind(String materialText) {
            button.setText(materialText);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click event
                }
            });
        }
    }

    // Method to update data
    public void updateData(List<String> newMaterials) {
        materials.clear();
        materials.addAll(newMaterials);
        notifyDataSetChanged(); // This will refresh the RecyclerView with the new data
    }

}
