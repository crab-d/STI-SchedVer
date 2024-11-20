package com.example.test2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentInfoActivity extends AppCompatActivity {

    private TextView nameTextView, logout, sectionTextView, studentNumberTextView, day, emptyMsg, status;
    private ImageView studentPicImageView;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        String gender = getIntent().getStringExtra("gender");
        studentPicImageView = findViewById(R.id.studentPic);

        if (gender.equals("Male")) {
            studentPicImageView.setImageResource(R.drawable.men_shs);
        } else {
            studentPicImageView.setImageResource(R.drawable.women_shs);
        }

        day = findViewById(R.id.day);
        emptyMsg = findViewById(R.id.msg);

        nameTextView = findViewById(R.id.text_name);
        sectionTextView = findViewById(R.id.text_section);
        studentNumberTextView = findViewById(R.id.text_studentNumber);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(StudentInfoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(0,0);

        });

        String name = getIntent().getStringExtra("name");
        String section = getIntent().getStringExtra("section");
        String studentNumber = getIntent().getStringExtra("studentNumber");

        nameTextView.setText("Name: " + name);
        sectionTextView.setText("Section: " + section);
        studentNumberTextView.setText("Student Number: " + studentNumber);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);

        fetchScheduleData(section);
        scheduleAdapter.notifyDataSetChanged();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("grade12").document("ITM302");

        status = findViewById(R.id.status);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Boolean earlyOut = task.getResult().getBoolean("earlyOut");

                // Check the value of earlyOut
                if (earlyOut != null && earlyOut) {
                    // If true, display the status
                    status.setText("Early out permission granted");
                    status.setVisibility(View.VISIBLE);
                } else {
                    // If false, hide the status
                    status.setVisibility(View.GONE);
                }
            } else {
                Log.e("Firestore", "Error getting document", task.getException());
            }
        });

    }

    private void fetchScheduleData(String section) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        String currentDay = sdf.format(new Date()).toLowerCase();
        day.setText(currentDay);
            db.collection("grade12")
                    .document(section)
                    .collection(currentDay)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String subject = document.getString("subject");
                                String time = document.getString("time");
                                String room = document.getString("room");
                                String professor = document.getString("professor");

                                scheduleList.add(new Schedule(currentDay, subject, time, room, professor));
                            }

                            scheduleAdapter.notifyDataSetChanged();
                        } else {
                            String msg = "No schedule for today";
                            emptyMsg.setVisibility(View.VISIBLE);
                            emptyMsg.setText(msg);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ScheduleActivity", "Error getting documents: ", e);
                    });

    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

}