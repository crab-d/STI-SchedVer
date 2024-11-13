package com.example.test2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView  nameTextView, emailTextView, studentNumberTextView, sectionTextView, logout;
    private ImageView qrImageView, studentPicImageView;
    

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
        
        
        
        
        logout = findViewById(R.id.logout);

        // Get data from intent or fallback to SharedPreferences if necessary
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String studentNumber = getIntent().getStringExtra("studentNumber");
        String section = getIntent().getStringExtra("section");
        String userId = getIntent().getStringExtra("userID");
        String gender = getIntent().getStringExtra("gender");

        if (userId == null) {
            name = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("name", null);
            email = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("email", null);
            studentNumber = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("studentNumber", null);
            section = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("section", null);
            userId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("userID", null);
            gender = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("gender", null);
        }

        nameTextView.setText("Name: " + name);
        emailTextView.setText("Email: " + email);
        studentNumberTextView.setText("Student Number: " + studentNumber);
        sectionTextView.setText("Section: " + section);
        
        
        if (gender.equals("Male")) {
            studentPicImageView.setImageResource(R.drawable.men_shs);
        } else {
            studentPicImageView.setImageResource(R.drawable.women_shs);
        }

        
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
            startActivity(intent);
            finish();
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
}