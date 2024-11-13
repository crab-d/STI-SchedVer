package com.example.test2;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, studentNumberEditText;
    private Spinner sectionSpinner, genderSpinner;
    private TextView signupButton;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        studentNumberEditText = findViewById(R.id.studentNumberEditText);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        signupButton = findViewById(R.id.signupButton);
        genderSpinner = findViewById(R.id.genderSpinner);

        // Set up the section spinner with options
        String[] sections = {"ITM101", "ITM102", "STEM101", "ABM101"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sections);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        
        String[] gender = {"Male", "Female"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter1);

        // Set up button click listener
        signupButton.setOnClickListener(view -> signUpUser());
    }

    private void signUpUser() {
        // Get input values
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String studentNumber = studentNumberEditText.getText().toString().trim();
        String section = sectionSpinner.getSelectedItem().toString();  // Get selected section from Spinner
        String gender = genderSpinner.getSelectedItem().toString();
        
        // Validate required fields
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!email.endsWith("@cubao.sti.edu.ph")) {
            emailEditText.setError("Only email from STI cubao are allowed");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        if (TextUtils.isEmpty(studentNumber)) {
            studentNumberEditText.setError("Student Number is required");
            return;
        }

        if (studentNumber.length() != 11) {
            studentNumberEditText.setError("Student Number must be 11 digits");
            return;
        }

        if (TextUtils.isEmpty(section)) {
            Toast.makeText(this, "Section is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email already exists in Firestore
        firestore.collection("users")
                .whereEqualTo("email", email)  // Query for existing email
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Email already exists
                        emailEditText.setError("Email already exists, message us for assistance.");
                    } else {
                        // Proceed with registration
                        generateUserIdAndRegister(name, email, password, studentNumber, section, gender);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void generateUserIdAndRegister(String name, String email, String password, String studentNumber, String section, String gender) {
    // Reference to the counter document for tracking user ID
    DocumentReference counterRef = firestore.collection("counters").document("userCounter");

    firestore.runTransaction((Transaction.Function<Void>) transaction -> {
        // Get the current count for generating unique user ID
        DocumentSnapshot counterDoc = transaction.get(counterRef);

        long currentCount;
        if (counterDoc.exists()) {
            currentCount = counterDoc.getLong("count") != null ? counterDoc.getLong("count") : 0;
        } else {
            currentCount = 0;
        }
        long newCount = currentCount + 1;
        String userId = "user" + newCount;

        // Update the counter document
        transaction.set(counterRef, new HashMap<String, Object>() {{
            put("count", newCount);
        }});

        // Create user data map
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("password", password);
        user.put("studentNumber", studentNumber);
        user.put("section", section);
                user.put("gender", gender);

        // Set the user data with the generated user ID as the document ID
        transaction.set(firestore.collection("users").document(userId), user);

        return null;
    }).addOnSuccessListener(aVoid -> {
        Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
        // You can navigate to the login or home screen here
    }).addOnFailureListener(e -> {
        Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    });
}
}