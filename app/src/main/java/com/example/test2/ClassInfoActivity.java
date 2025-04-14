package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClassInfoActivity extends AppCompatActivity {

    private TextView logout, earlyOut, status, schedule, sectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_info);

        earlyOut = findViewById(R.id.earlyOut);
        status = findViewById(R.id.status);
        sectionName = findViewById(R.id.section);

        String section = getIntent().getStringExtra("sectionName");
        String gradeLevel = getIntent().getStringExtra("gradeLevel");
        
        sectionName.setText(section);
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("schedule").document("shs").collection(gradeLevel).document(section);
        
        
        earlyOut.setOnClickListener(v -> {
            docRef.update("earlyOut", true)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "earlyOut set to true");
                        Toast.makeText(this, "Early out granted", Toast.LENGTH_SHORT).show();
                        status.setText("Early out permission granted");
                        status.setVisibility(View.VISIBLE);
                        earlyOut.setText("Revoke Permission");
                        earlyOut.setOnClickListener(a -> {
                            docRef.update("earlyOut", false).addOnSuccessListener(s -> {
                                status.setVisibility(View.GONE);
                                        earlyOut.setText("Early Out");
                                        return;
                            });
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error updating earlyOut", e);
                        Toast.makeText(this, "Failed to grant early out", Toast.LENGTH_SHORT).show();
                    });
        });

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Boolean earlyOut1 = task.getResult().getBoolean("earlyOut");

                // Check the value of earlyOut
                if (earlyOut1) {
                    // If true, display the status
                    status.setText("Early out permission granted");
                    status.setVisibility(View.VISIBLE);
                        earlyOut.setText("Revoke Permission");
                        earlyOut.setOnClickListener(a -> {
                            docRef.update("earlyOut", false).addOnSuccessListener(s -> {
                                status.setVisibility(View.GONE);
                                        earlyOut.setText("Early Out");
                                        return;
                            });
                        });
                } else {
                    // If false, hide the status
                    status.setVisibility(View.GONE);
                }
            } else {
                Log.e("Firestore", "Error getting document", task.getException());
            }
        });

        schedule = findViewById(R.id.schedule);
        schedule.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("section", section);
                intent.putExtra("gradeLevel", "grade12");
                intent.putExtra("status", "shs");
            startActivity(intent);
            overridePendingTransition(0,0);
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

        //class List
        TextView classlist = findViewById(R.id.classList);
        classlist.setOnClickListener(view -> {
            Intent intent = new Intent(this, StudentListActivity.class);
            intent.putExtra("section", section);
            startActivity(intent);
            overridePendingTransition(0,0);
        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}