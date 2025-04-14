package com.example.test2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Schedule> scheduleList;

    public ScheduleAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        // Set subject, time, room, professor, and day
        holder.subjectTextView.setText(schedule.getSubject());
        holder.timeTextView.setText(schedule.getTime());
        holder.roomTextView.setText(schedule.getRoom());
        holder.professorTextView.setText(schedule.getProfessor());

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView, timeTextView, roomTextView, professorTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            roomTextView = itemView.findViewById(R.id.roomTextView);
            professorTextView = itemView.findViewById(R.id.professorTextView);

        }
    }
}
