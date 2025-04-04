package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private TextView loginStatusText, loginButton, signupBTN;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetEarlyOutIfNeeded();

        boolean isLoggedIn = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
        boolean isStaff = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                .getBoolean("isStaff", false);
        boolean isTeacher = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                .getBoolean("isTeacher", false);

        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, StudentDashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            overridePendingTransition(0,0);
        } else if (isStaff) {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            overridePendingTransition(0,0);
        } else if (isTeacher) {
            Intent intent = new Intent(this, TeacherActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
            overridePendingTransition(0,0);
        }

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupBTN = findViewById(R.id.signupBTN);
        loginStatusText = findViewById(R.id.loginStatusText);

        // Set up login button click listener
        loginButton.setOnClickListener(view -> loginUser());

        signupBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);

            }
        });

        Log.d("MainActivity", "test");

    }

    private void loginUser() {
        // Get user input
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

//        if (email.equals("guard") && password.equals("guard")) {
//            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
//                    .edit()
//                    .putBoolean("isStaff", true)
//                    .apply();
//
//            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
//            startActivity(intent);
//            finish();
//        } else if (email.equals("jayven.menes@cubao.sti.edu.ph") && password.equals("qwertyui")) {
//            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
//                    .edit()
//                    .putBoolean("isTeacher", true)
//                    .apply();
//            Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (email.equals("guard") && password.equals("guard")) {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isStaff", true)
                    .apply();

            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);

            finish();
            overridePendingTransition(0,0);

//        } else if (email.equals("jayven.menes@cubao.sti.edu.ph") && password.equals("qwertyui")) {
//            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
//                    .edit()
//                    .putBoolean("isTeacher", true)
//                    .apply();
//            Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
//            startActivity(intent);
//            finish();
//        }
        } else {
            firestore.collection("teachers")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    String name = documentSnapshot.getString("name");
                                    getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                                            .edit()
                                            .putBoolean("isTeacher", true)
                                            .putString("name", name)
                                            .putString("email", email)
                                            .apply();

                                    Intent intent = new Intent(this, TeacherActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0,0);
                                } else {
                                    firestore.collection("users")
                                            .whereEqualTo("email", email)
                                            .whereEqualTo("password", password)
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot querySnapshot = task.getResult();
                                                    if (!querySnapshot.isEmpty()) {
                                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                                        String name = documentSnapshot.getString("name");
                                                        String studentNumber = documentSnapshot.getString("studentNumber");
                                                        String section = documentSnapshot.getString("section");
                                                        String userID = documentSnapshot.getId();  // This is the userID (document name)
                                                        String gender = documentSnapshot.getString("gender");

                                                        // Save login state and user details in SharedPreferences
                                                        getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                                                                .edit()
                                                                .putBoolean("isLoggedIn", true)
                                                                .putString("name", name)
                                                                .putString("email", email)
                                                                .putString("studentNumber", studentNumber)
                                                                .putString("section", section)
                                                                .putString("userID", userID)
                                                                .putString("gender", gender)
                                                                .apply();


                                                        Intent intent = new Intent(MainActivity.this, StudentDashboardActivity.class);
                                                        intent.putExtra("name", name);
                                                        intent.putExtra("email", email);
                                                        intent.putExtra("studentNumber", studentNumber);
                                                        intent.putExtra("section", section);
                                                        intent.putExtra("userID", userID);
                                                        intent.putExtra("gender", gender);
                                                        startActivity(intent);
                                                        overridePendingTransition(0,0);

                                                        finish();
                                                        overridePendingTransition(0,0);

                                                    } else {
                                                        loginStatusText.setVisibility(TextView.VISIBLE);
                                                        loginStatusText.setText("Incorrect email or password. Please try again.");
                                                    }
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });

        }
    }

    private void resetEarlyOutIfNeeded() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String lastResetDate = sharedPreferences.getString("lastResetDate", null);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!today.equals(lastResetDate)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("grade12").document("ITM302");

            // Reset the earlyOut field to false
            docRef.update("earlyOut", false)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "earlyOut reset to false"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error resetting earlyOut", e));

            // Update the last reset date in SharedPreferences
            sharedPreferences.edit().putString("lastResetDate", today).apply();
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}