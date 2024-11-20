package com.example.test2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, studentNumberEditText;
    private AutoCompleteTextView auto_complete_section, auto_complete_gender;
    private TextView signupButton;
    private ArrayAdapter<String> adapterItems, genderAdapterItems;
    ProgressBar progressBar;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.progressBar);
        LinearLayout layout = findViewById(R.id.layout);
        layout.setVisibility(View.GONE); // Hide layout initially
        progressBar.setVisibility(View.VISIBLE); // Show progress bar

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        studentNumberEditText = findViewById(R.id.studentNumberEditText);
        signupButton = findViewById(R.id.signupButton);

        auto_complete_section = findViewById(R.id.auto_complete_section);

        // Initialize gender dropdown
        String[] gender = {"Male", "Female"};
        auto_complete_gender = findViewById(R.id.auto_complete_gender);
        genderAdapterItems = new ArrayAdapter<>(this, R.layout.list_item, gender);
        auto_complete_gender.setAdapter(genderAdapterItems);

        // Fetch section data
        fetchSections();

        signupButton.setOnClickListener(view -> signUpUser());
    }

    private void fetchSections() {
        List<String> sectionList = new ArrayList<>();

        // Fetch sections for both grade11 and grade12
        Task<QuerySnapshot> grade12Task = firestore.collection("grade12").get();
        Task<QuerySnapshot> grade11Task = firestore.collection("grade11").get();

        // Combine tasks
        Task<List<QuerySnapshot>> combinedTask = Tasks.whenAllSuccess(grade12Task, grade11Task);

        combinedTask.addOnSuccessListener(querySnapshots -> {
            // Add data from grade12
            QuerySnapshot grade12Snapshot = querySnapshots.get(0);
            for (QueryDocumentSnapshot document : grade12Snapshot) {
                sectionList.add(document.getId());
            }

            // Add data from grade11
            QuerySnapshot grade11Snapshot = querySnapshots.get(1);
            for (QueryDocumentSnapshot document : grade11Snapshot) {
                sectionList.add(document.getId());
            }

            // Update the adapter with fetched sections
            adapterItems = new ArrayAdapter<>(this, R.layout.list_item, sectionList);
            auto_complete_section.setAdapter(adapterItems);

            // Show layout and hide progress bar
            progressBar.setVisibility(View.GONE);
            LinearLayout layout = findViewById(R.id.layout);
            layout.setVisibility(View.VISIBLE);

        }).addOnFailureListener(e -> {
            // Handle errors
            Toast.makeText(this, "Failed to fetch sections: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("FirestoreError", "Error fetching sections", e);

            progressBar.setVisibility(View.GONE);
            LinearLayout layout = findViewById(R.id.layout);
            layout.setVisibility(View.VISIBLE); // Still show the form to avoid blocking the user
        });
    }

    private void signUpUser() {
        // Get input values
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String studentNumber = studentNumberEditText.getText().toString().trim();

        // Get selected section and gender from AutoCompleteTextView
        String section = auto_complete_section.getText().toString().trim();
        String gender =  auto_complete_gender.getText().toString().trim();

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
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        emailEditText.setError("Account already exists. Not you? message us for assistance.");
                    } else {
                        generateUserIdAndRegister(name, email, password, studentNumber, section, gender);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void generateUserIdAndRegister(String name, String email, String password, String studentNumber, String section, String gender) {
        DocumentReference counterRef = firestore.collection("counters").document("userCounter");

        firestore.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot counterDoc = transaction.get(counterRef);

            long currentCount;
            if (counterDoc.exists()) {
                currentCount = counterDoc.getLong("count") != null ? counterDoc.getLong("count") : 0;
            } else {
                currentCount = 0;
            }
            long newCount = currentCount + 1;
            String userId = "user" + newCount;

            // Update counter document
            transaction.set(counterRef, new HashMap<String, Object>() {{
                put("count", newCount);
            }});

            // Create new user document
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("password", password);
            user.put("studentNumber", studentNumber);
            user.put("section", section);
            user.put("gender", gender);

            transaction.set(firestore.collection("users").document(userId), user);

            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            // Redirect to another activity (if needed)
        }).addOnFailureListener(e -> {
            Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
