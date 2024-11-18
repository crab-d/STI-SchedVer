package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherActivity extends AppCompatActivity {

    private TextView teacherSched, advisoryClass, logout;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher);

        teacherSched = findViewById(R.id.schedule);
        advisoryClass = findViewById(R.id.advisoryClass);
        logout = findViewById(R.id.logout);

        advisoryClass.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherActivity.this, AdvisoryClassActivity.class);
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