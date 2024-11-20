package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherActivity extends AppCompatActivity {

    private TextView teacherSched, advisoryClass, logout, name, email;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        String intentName = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("name", null);
        String intentEmail = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("email", null);

        name = findViewById(R.id.name);
        name.setText("Name : " + intentName);

        email = findViewById(R.id.email);
        email.setText("Email : " + intentEmail);


        teacherSched = findViewById(R.id.schedule);
        advisoryClass = findViewById(R.id.advisoryClass);
        logout = findViewById(R.id.logout);

        advisoryClass.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherActivity.this, AdvisoryClassActivity.class);
            intent.putExtra("advisor", "Jayven Menes");
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