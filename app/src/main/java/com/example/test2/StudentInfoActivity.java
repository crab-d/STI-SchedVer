package com.example.test2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentInfoActivity extends AppCompatActivity {

    private TextView nameTextView, logout, sectionTextView, studentNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        nameTextView = findViewById(R.id.text_name);
        sectionTextView = findViewById(R.id.text_section);
        studentNumberTextView = findViewById(R.id.text_studentNumber);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(StudentInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
        });

        // Retrieve data from intent
        String name = getIntent().getStringExtra("name");
        String section = getIntent().getStringExtra("section");
        String studentNumber = getIntent().getStringExtra("studentNumber");

        // Display data
        nameTextView.setText("Name: " + name);
        sectionTextView.setText("Section: " + section);
        studentNumberTextView.setText("Student Number: " + studentNumber);
    }
}