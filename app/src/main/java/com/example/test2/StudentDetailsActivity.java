package com.example.test2;

import android.content.Intent;
import android.icu.number.IntegerWidth;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
//Teacher
public class StudentDetailsActivity extends AppCompatActivity {
    TextView enrollment_statusD, nameD, sectionD, emailD, genderD, studentNumberD;
    ImageView studentPicture;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        
        String studentNumber = getIntent().getStringExtra("studentNumber");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        nameD = findViewById(R.id.nameD);
        sectionD = findViewById(R.id.sectionD);
        emailD = findViewById(R.id.emailD);
        genderD = findViewById(R.id.genderD);
        studentNumberD = findViewById(R.id.studentNumberD);
        studentPicture = findViewById(R.id.studentPicture);
        enrollment_statusD = findViewById(R.id.enrollment_statusD);
        
        db.collection("users").document(studentNumber).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
            	String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String gender = documentSnapshot.getString("gender");
                    
                   // String section = documentSnapshot.getString("section");
                    String enrollment_status = documentSnapshot.getString("status");
                    
                    nameD.setText("Name: " + name);
                    studentNumberD.setText("Student Number: " + studentNumber);
                    emailD.setText("Email: " + email);
                    genderD.setText("Gender: " + gender);
                    enrollment_statusD.setText("Status: " + enrollment_status);
                    
                    if (gender.equalsIgnoreCase("male")) {
                        studentPicture.setImageResource(R.drawable.men_shs);
                    } else {
                        studentPicture.setImageResource(R.drawable.women_shs);
                    }
                    
                    db.collection("term").document("semester").get().addOnSuccessListener(d -> {
                        String sem = d.getString("sem");
                            if(sem.equalsIgnoreCase("sem2")) {
                            	String section = documentSnapshot.getString("section1");
                                sectionD.setText("Section: " + section);
                            } else {
                                String section = documentSnapshot.getString("section");
                               sectionD.setText("Section: " + section);
                            }
                    });
                    
                    
            } else {
                Toast.makeText(this, "Error in getting student data", Toast.LENGTH_SHORT).show();
            }
        });
        
        TextView earlyOut_btn = findViewById(R.id.earlyOut_btn);
        TextView status = findViewById(R.id.status);
        
        
        earlyOut_btn.setOnClickListener(v -> {
            db.collection("users").document(studentNumber).update("earlyOut", true).addOnSuccessListener(documentSnapshot -> {
                        status.setText("Early out permission granted");
                        status.setVisibility(View.VISIBLE);
                
                        earlyOut_btn.setText("Revoke permission");
                        earlyOut_btn.setOnClickListener(v1 -> {
                            db.collection("users").document(studentNumber).update("earlyOut", false).addOnSuccessListener(d1 -> {
                                status.setText("Early out permission revoked");
                                        status.setVisibility(View.GONE);
                                        earlyOut_btn.setText("Early out");
                                        return;
                            });
                        });
            });
        });
            
        
        
        db.collection("users").document(studentNumber).get().addOnSuccessListener(d -> {
            if(d.exists()) {
            	Boolean earlyOut_db = d.getBoolean("earlyOut");
                    if(earlyOut_db) {
                        status.setText("Early out permission granted");
                        status.setVisibility(View.VISIBLE);
                        earlyOut_btn.setText("Revoke permission");
                        earlyOut_btn.setOnClickListener(v -> {
                            db.collection("users").document(studentNumber).update("earlyOut", false).addOnSuccessListener(d1 -> {
                                status.setText("Early out permission revoked");
                                        status.setVisibility(View.GONE);
                                        earlyOut_btn.setText("Early out");
                                        return;
                            });
                        });
                    }
            }
        });
            
        
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
                
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                overridePendingTransition(0,0);
                
                
        });
    }
    
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
