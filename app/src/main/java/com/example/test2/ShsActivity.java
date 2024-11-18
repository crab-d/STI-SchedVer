package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShsActivity extends AppCompatActivity {

    TextView logout;
    private RecyclerView recyclerView;
    private SectionAdapter sectionAdapter;
    private List<Section> sectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shs);

        // Initialize RecyclerView and other views
        recyclerView = findViewById(R.id.sectionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your section list (e.g., from Firestore or a static list)
        sectionList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("grade12").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sectionList.clear(); // Clear the existing list to avoid duplication
                for (DocumentSnapshot document : task.getResult()) {

                    String sectionName = document.getId();
                    sectionList.add(new Section(sectionName));
                }
                sectionAdapter.notifyDataSetChanged(); // Notify adapter about the new data
            } else {
                Log.e("Firestore", "Error getting sections", task.getException());
            }
        });


        // Create and set the adapter
        sectionAdapter = new SectionAdapter(sectionList, this);
        recyclerView.setAdapter(sectionAdapter);


        sectionAdapter.setOnItemClickListener(new SectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Get the clicked section
                Section clickedSection = sectionList.get(position);

                // Create an Intent to open ScheduleActivity and pass the section name
                Intent intent = new Intent(ShsActivity.this, ScheduleActivity.class);
                intent.putExtra("section", clickedSection.getSectionName());
                startActivity(intent);
                overridePendingTransition(0,0);

            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
