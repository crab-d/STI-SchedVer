package com.example.test2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Constructor
    public StudentAdapter(List<Student> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    // ViewHolder class to hold individual item views
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView studentNumberTextView;
        TextView sectionTextView;
        TextView genderTextView;
        ImageView studentPicture;

        // Constructor for ViewHolder, setting up the views
        public StudentViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            studentNumberTextView = itemView.findViewById(R.id.studentNumberTextView);
            sectionTextView = itemView.findViewById(R.id.sectionTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            studentPicture = itemView.findViewById(R.id.studentPic);


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

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        // Return the new ViewHolder
        return new StudentViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        // Bind the student data to the TextViews
        holder.nameTextView.setText(student.getName());
        holder.emailTextView.setText(student.getEmail());
        holder.studentNumberTextView.setText(student.getStudentNumber());
        holder.sectionTextView.setText(student.getSection());
        holder.genderTextView.setText(student.getGender());
        if (student.getGender().equalsIgnoreCase("male")) {
            holder.studentPicture.setImageResource(R.drawable.men_shs);
        } else {
            holder.studentPicture.setImageResource(R.drawable.women_shs);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
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
