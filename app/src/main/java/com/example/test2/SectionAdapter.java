package com.example.test2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private List<Section> sectionList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SectionAdapter(List<Section> sectionList, Context context) {
        this.sectionList = sectionList;
        this.context = context;
    }

    // ViewHolder class to hold individual item views
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionNameTextView;

        // Constructor for ViewHolder, setting up the TextView and click listener
        public SectionViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            sectionNameTextView = itemView.findViewById(R.id.sectionTextView); // Adjust as per your layout
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        // Return the new ViewHolder
        return new SectionViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section section = sectionList.get(position);
        // Bind the section name to the TextView
        holder.sectionNameTextView.setText(section.getSectionName());
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Method to set the click listener from outside the adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
