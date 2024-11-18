package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;

public class DaySchedActivity extends AppCompatActivity {

    TextView dayTextView, emptyMsg, logout;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_sched);
        emptyMsg = findViewById(R.id.msg);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(scheduleAdapter);

        String section = getIntent().getStringExtra("section");
        String day = getIntent().getStringExtra("day");

        // Validate section and day before proceeding
        if (section == null || section.isEmpty()) {
            Log.e("DaySchedActivity", "Section is null or empty");
            Toast.makeText(this, "Invalid section", Toast.LENGTH_SHORT).show();
            return;
        }

        if (day == null || day.isEmpty()) {
            Log.e("DaySchedActivity", "Day is null or empty");
            Toast.makeText(this, "Invalid day", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the dayTextView (initialize it)
        dayTextView = findViewById(R.id.dayTextView);  // Make sure the ID matches your layout
        dayTextView.setText(day);

        // Fetch schedule data
        fetchScheduleData(section, day);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(0,0);

        });
    }

    private void fetchScheduleData(String section, String day) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("grade12")
                .document(section)
                .collection(day)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String subject = document.getString("subject");
                            String time = document.getString("time");
                            String room = document.getString("room");
                            String professor = document.getString("professor");

                            scheduleList.add(new Schedule(day, subject, time, room, professor));
                        }

                        scheduleAdapter.notifyDataSetChanged();
                    } else {
                        String msg = "No schedule for this day";
                        emptyMsg.setVisibility(View.VISIBLE);
                        emptyMsg.setText(msg);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DaySchedActivity", "Error getting documents: ", e);
                    Toast.makeText(this, "Failed to get schedule on this day, please try again. If error persists kindly contact us for maintenance.", Toast.LENGTH_SHORT).show();
                });
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

}
