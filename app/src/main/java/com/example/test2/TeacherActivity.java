package com.example.test2;

import android.content.Intent;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherActivity extends AppCompatActivity {

    private TextView teacherSched, advisoryClass, logout, name, email, statusD;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        String intentName = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("name", null);
        String intentEmail = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("email", null);
        String status = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("status", null);
        String codename = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("codename", null);

        
        name = findViewById(R.id.name);
        name.setText("Name: " + intentName);

        email = findViewById(R.id.email);
        email.setText("Email: " + intentEmail);
        
        statusD = findViewById(R.id.status);
        statusD.setText("Status: " + status);


        teacherSched = findViewById(R.id.schedule);
        advisoryClass = findViewById(R.id.advisoryClass);
        logout = findViewById(R.id.logout);
        
        teacherSched.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScheduleActivity.class);
                intent.putExtra("codename", codename);
                intent.putExtra("status", "teacher");
                startActivity(intent);
                overridePendingTransition(0,0);
        });

        advisoryClass.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherActivity.this, AdvisoryClassActivity.class);
            intent.putExtra("advisor", codename);
            startActivity(intent);
            overridePendingTransition(0,0);
        });

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