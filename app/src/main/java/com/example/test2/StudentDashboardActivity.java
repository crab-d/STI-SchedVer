package com.example.test2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView  statusTv, nameTextView, gradeLevelTv, emailTextView, studentNumberTextView, sectionTextView, logout, schedule;
    private ImageView qrImageView, studentPicImageView;
    private String section, gradeLevel, studentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Initialize UI elements
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        studentNumberTextView = findViewById(R.id.studentNumberTextView);
        sectionTextView = findViewById(R.id.sectionTextView);
        qrImageView = findViewById(R.id.qrImageView);
        studentPicImageView = findViewById(R.id.studentPic);
        schedule = findViewById(R.id.schedule);
        gradeLevelTv = findViewById(R.id.gradeLevelTv);
        statusTv = findViewById(R.id.status);
        logout = findViewById(R.id.logout);

        // Get data from intent or fallback to SharedPreferences if necessary
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
         studentNumber = getIntent().getStringExtra("studentNumber");
        section = getIntent().getStringExtra("section");
        String userId = getIntent().getStringExtra("userID");
        String gender = getIntent().getStringExtra("gender");
         gradeLevel = getIntent().getStringExtra("gradeLevel");
        String status = getIntent().getStringExtra("status");
        
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, ScheduleActivity.class);
                intent.putExtra("section", section);
                    intent.putExtra("status", "shs");
                    intent.putExtra("gradeLevel", gradeLevel);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        if (userId == null) {
            name = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("name", null);
            email = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("email", null);
            studentNumber = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("studentNumber", null);
            section = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("section", null);
            userId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("userID", null);
            gender = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("gender", null);
            status = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("status", null);
           gradeLevel = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("gradeLevel", null);
        }

        nameTextView.setText("Name: " + name);
        emailTextView.setText("Email: " + email);
        studentNumberTextView.setText("Student Number: " + studentNumber);
        sectionTextView.setText("Section: " + section);
        gradeLevelTv.setText("Grade Level: " + gradeLevel);
        statusTv.setText("Status: " + status);
        
        if (gender.equals("Male")) {
            studentPicImageView.setImageResource(R.drawable.men_shs);
        } else {
            studentPicImageView.setImageResource(R.drawable.women_shs);
        }
        
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("schedule").document("shs").collection(gradeLevel).document(section);

        TextView earlyOut_status = findViewById(R.id.earlyOut_status);
        

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Boolean earlyOut = documentSnapshot.getBoolean("earlyOut");

                if (earlyOut) {
                    earlyOut_status.setText("Early out permission granted");
                    earlyOut_status.setVisibility(View.VISIBLE);
                } else {
                db.collection("users").document(studentNumber).get().addOnSuccessListener(d -> {
                            
                    if (d.exists()) {
                        Boolean earlyOut1 = d.getBoolean("earlyOut");
                                if (earlyOut1) {
                    earlyOut_status.setText("Early out permission granted");
                    earlyOut_status.setVisibility(View.VISIBLE);
                    }
                                }
                });
                    
            }
                    
            } 
        });
        
        
        
        

        
        if (userId != null) {
            generateQRCode(userId);
        } else {
            Log.d("StudentDashboard", "User ID is null, QR code not generated.");
        }
        
        
        
        
        
        
        

        // Logout button functionality
        logout.setOnClickListener(v -> {
            // Clear login state and user data in SharedPreferences
            getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();

            // Redirect to login screen
            Intent intent = new Intent(StudentDashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);

        });
        
        Log.d("StudentDashboard", "User ID: " + userId);
    }

    private void generateQRCode(String data) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(data, com.google.zxing.BarcodeFormat.QR_CODE, 500, 500);
            qrImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}