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

    private TextView btn_scan, logout;
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
                startActivity(intent);
                finish();
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

                            // Pass data to StudentInfoActivity
                            Intent intent = new Intent(ScannerActivity.this, StudentInfoActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("section", section);
                            intent.putExtra("studentNumber", studentNumber);
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
}