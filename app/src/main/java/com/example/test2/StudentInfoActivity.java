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
import java.util.Set;
import java.util.Map;

//Guard
public class StudentInfoActivity extends AppCompatActivity {

    private TextView nameTextView, grade_level, enrollment_status, logout, sectionTextView, studentNumberTextView, day, emptyMsg, status;
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

        grade_level = findViewById(R.id.grade_level);
        day = findViewById(R.id.day);
        emptyMsg = findViewById(R.id.msg);
        enrollment_status = findViewById(R.id.enrollment_status);
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
        String statusD = getIntent().getStringExtra("statusD");
        String name = getIntent().getStringExtra("name");
        String section = getIntent().getStringExtra("section");
        String studentNumber = getIntent().getStringExtra("studentNumber");
        String gradeLevel = getIntent().getStringExtra("gradeLevel");
        
        nameTextView.setText("Name: " + name);
        sectionTextView.setText("Section: " + section);
        studentNumberTextView.setText("Student Number: " + studentNumber);
        enrollment_status.setText("Status: enrolled ");
        grade_level.setText("Grade Level: " + gradeLevel);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);
        

        fetchScheduleData(gradeLevel, section);
        scheduleAdapter.notifyDataSetChanged();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("schedule").document("shs").collection(gradeLevel).document(section);

        status = findViewById(R.id.status);
        

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Boolean earlyOut = documentSnapshot.getBoolean("earlyOut");

                if (earlyOut) {
                    status.setText("Early out permission granted");
                    status.setVisibility(View.VISIBLE);
                } else {
                db.collection("users").document(studentNumber).get().addOnSuccessListener(d -> {
                    if (d.exists()) {
                        Boolean earlyOut1 = d.getBoolean("earlyOut");
                                if (earlyOut1) {
                    status.setText("Early out permission granted");
                    status.setVisibility(View.VISIBLE);
                    }
                                }
                });
                    
            }
                    
            } 
        });

    }

    private void fetchScheduleData(String gradeLevel, String section) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        String currentDay = sdf.format(new Date()).toLowerCase();
        day.setText(currentDay);
            db.collection("schedule")
                    .document("shs")
        .collection(gradeLevel)
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
                            progressBar.setVisibility(View.GONE);
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