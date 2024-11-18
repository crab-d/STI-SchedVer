package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    private ScheduleAdapter scheduleAdapter;
//    private List<Schedule> scheduleList;
    TextView monday, tuesday, wednesday, thursday, friday, saturday, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        String section = getIntent().getStringExtra("section");
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);

        monday.setOnClickListener(v -> {
            Intent intent = new Intent(this, DaySchedActivity.class);
            intent.putExtra("day", "monday");
            intent.putExtra("section", section);
            startActivity(intent);
            overridePendingTransition(0,0);

        });

        tuesday.setOnClickListener(v -> {
            Intent intent = new Intent(this, DaySchedActivity.class);
            intent.putExtra("day", "tuesday");
            intent.putExtra("section", section);
            startActivity(intent);
            overridePendingTransition(0, 0);

        });

        wednesday.setOnClickListener(v -> {
            Intent intent = new Intent(this, DaySchedActivity.class);
            intent.putExtra("day", "wednesday");
            intent.putExtra("section", section);
            startActivity(intent);
            overridePendingTransition(0, 0);

        });

        thursday.setOnClickListener(v -> {
            Intent intent = new Intent(this, DaySchedActivity.class);
            intent.putExtra("day", "thursday");
            intent.putExtra("section", section);

            startActivity(intent);
            overridePendingTransition(0, 0);

        });

        friday.setOnClickListener(v -> {
            Intent intent = new Intent(this, DaySchedActivity.class);
            intent.putExtra("day", "friday");
            intent.putExtra("section", section);

            startActivity(intent);
            overridePendingTransition(0, 0);

        });

        saturday.setOnClickListener(v -> {
            Intent intent = new Intent(this, DaySchedActivity.class);
            intent.putExtra("day", "Saturday");
            intent.putExtra("section", section);

            startActivity(intent);
            overridePendingTransition(0, 0);

        });
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        });





//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        scheduleList = new ArrayList<>();
//
//        String section = getIntent().getStringExtra("section");
//
//        scheduleAdapter = new ScheduleAdapter(scheduleList);
//        recyclerView.setAdapter(scheduleAdapter);
//
//        fetchScheduleData(section);
//        sortScheduleByDay(scheduleList);
//        scheduleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }


//    private void fetchScheduleData(String section) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
//        scheduleList.clear();
//
//        for (String day : days) {
//            db.collection("grade12")
//                    .document(section)
//                    .collection(day)
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        if (!queryDocumentSnapshots.isEmpty()) {
//                            for (DocumentSnapshot document : queryDocumentSnapshots) {
//                                String subject = document.getString("subject");
//                                String time = document.getString("time");
//                                String room = document.getString("room");
//                                String professor = document.getString("professor");
//
//                                scheduleList.add(new Schedule(day, subject, time, room, professor));
//                            }
//
//                            sortScheduleByDay(scheduleList);
//                            scheduleAdapter.notifyDataSetChanged();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e("ScheduleActivity", "Error getting documents: ", e);
//                    });
//        }
//    }
//
//    private void sortScheduleByDay(List<Schedule> scheduleList) {
//        Map<String, Integer> dayOrder = new HashMap<>();
//        dayOrder.put("monday", 0);
//        dayOrder.put("tuesday", 1);
//        dayOrder.put("wednesday", 2);
//        dayOrder.put("thursday", 3);
//        dayOrder.put("friday", 4);
//        dayOrder.put("saturday", 5);
//
//        Collections.sort(scheduleList, (s1, s2) ->
//                Integer.compare(dayOrder.get(s1.getDay().toLowerCase()), dayOrder.get(s2.getDay().toLowerCase()))
//        );
//    }
}