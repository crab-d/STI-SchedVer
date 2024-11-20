package com.example.test2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvisoryClassAdapter extends RecyclerView.Adapter<AdvisoryClassAdapter.SectionViewHolder> {

    private Context context;
    private List<Section> sectionList;

    public AdvisoryClassAdapter(Context context, List<Section> sectionList) {
        this.context = context;
        this.sectionList = sectionList;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        final Section section = sectionList.get(position);
        holder.sectionTextView.setText(section.getSectionName());

        holder.itemView.setOnClickListener(v -> {
            // When item is clicked, pass the section name to the next activity
            Intent intent = new Intent(context, ClassInfoActivity.class);
            intent.putExtra("sectionName", section.getSectionName());
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });
    }


    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTextView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionTextView = itemView.findViewById(R.id.sectionTextView); // Simple list item with text view
        }
    }
}

