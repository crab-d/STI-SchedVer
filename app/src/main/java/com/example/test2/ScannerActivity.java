package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.time.Instant;

public class ScannerActivity extends AppCompatActivity {

    private TextView btn_scan, logout, grade12, grade11;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Setup scan button
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> scancode());
        
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
                getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(ScannerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);

        });

        grade12 = findViewById(R.id.grade12);
        grade12.setOnClickListener(view -> {
            Intent intent = new Intent(this, ShsActivity.class);
            intent.putExtra("grade", "grade12");
            startActivity(intent);
            overridePendingTransition(0, 0);

        });

        grade11 = findViewById(R.id.grade11);
        grade11.setOnClickListener(view -> {
            Intent intent = new Intent(this, ShsActivity.class);
            intent.putExtra("grade", "grade11");
            startActivity(intent);
            overridePendingTransition(0, 0);

        });
    }

    private void scancode() {
        // Configure ScanOptions
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on\nVolume down to flash off");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    // Define the ActivityResultLauncher for scanning
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // Retrieve scanned user ID from QR code
            String userId = result.getContents();

            // Query Firestore using the scanned user ID
            firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Retrieve user data if document exists
                            String name = document.getString("name");
                            String section = document.getString("section");
                            String studentNumber = document.getString("studentNumber");
                            String gender = document.getString("gender");

                            // Pass data to StudentInfoActivity
                            Intent intent = new Intent(ScannerActivity.this, StudentInfoActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("section", section);
                            intent.putExtra("studentNumber", studentNumber);
                            intent.putExtra("gender", gender);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ScannerActivity.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ScannerActivity.this, "Error accessing database.", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    });
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}