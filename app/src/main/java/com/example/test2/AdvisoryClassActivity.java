package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdvisoryClassActivity extends AppCompatActivity {
    private TextView logout, ITM302;
    AdvisoryClassAdapter advisoryClassAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisory_class);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String advisor = getIntent().getStringExtra("advisor");

        List<Section> sections = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        db.collection("grade12")
                .whereEqualTo("advisor", advisor)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Create a new Section object using the document ID
                            Section section = new Section(document.getId());
                            sections.add(section); // Add the Section object to the list
                        }

                        // Log the sections to verify
                        Log.d("arraycheck", "section : " + sections.toString());

                        // Set up RecyclerView

                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        advisoryClassAdapter = new AdvisoryClassAdapter(this, sections); // Pass the list of Section objects
                        recyclerView.setAdapter(advisoryClassAdapter);
                    }
                });



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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}