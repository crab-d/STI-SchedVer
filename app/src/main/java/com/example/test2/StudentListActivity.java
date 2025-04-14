package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class StudentListActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<Student> studentList;
    StudentAdapter studentAdapter;
    String section;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

         db = FirebaseFirestore.getInstance();
         section = getIntent().getStringExtra("section");
        
        
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.studentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentList, this);
        recyclerView.setAdapter(studentAdapter);
        
        db.collection("term").document("semester").get().addOnSuccessListener(documentSnapshot -> {
			if(documentSnapshot.exists()) {
			 String sem = documentSnapshot.getString("sem");
				
				if (sem.equalsIgnoreCase("sem2")) {
                        studentInfo1();
        } else {
            studentInfo();
                    
        }
                    }
		});
        
        

        

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
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
    
    public void studentInfo() {
        
        db.collection("users")
                .whereEqualTo("section", section)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentList.clear();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        QuerySnapshot result = task.getResult();
                        if (result != null) {
                            for (DocumentSnapshot document : result) {
                                String name = document.getString("name");
                            String Lname = document.getString("lname");
                            String Fname = document.getString("fname");
                                String email = document.getString("email");
                                String studentNumber = document.getString("studentNumber");
                                String section = document.getString("section");
                                String gender = document.getString("gender");

                                Student student = new Student(Lname, Fname, name ,email, studentNumber, section, gender);
                                studentList.add(student);
                            }

                            Collections.sort(studentList, new Comparator<Student>() {
                                @Override
                                public int compare(Student s1, Student s2) {
                                    return s1.getLname().compareToIgnoreCase(s2.getLname());
                                }
                            });

                            studentAdapter.notifyDataSetChanged();
                        }



                    } else {
                        Toast.makeText(this, "error hinack ni gonzales database ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    public void studentInfo1() {
        
        db.collection("users")
                .whereEqualTo("section1", section)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentList.clear();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        QuerySnapshot result = task.getResult();
                        if (result != null) {
                            for (DocumentSnapshot document : result) {
                                String name = document.getString("name");
                            String Lname = document.getString("lname");
                            String Fname = document.getString("fname");
                                String email = document.getString("email");
                                String studentNumber = document.getString("studentNumber");
                                String section = document.getString("section");
                                String gender = document.getString("gender");

                                Student student = new Student(Lname, Fname, name ,email, studentNumber, section, gender);
                                studentList.add(student);
                            }

                            Collections.sort(studentList, new Comparator<Student>() {
                                @Override
                                public int compare(Student s1, Student s2) {
                                    return s1.getLname().compareToIgnoreCase(s2.getLname());
                                }
                            });

                            studentAdapter.notifyDataSetChanged();
                        }



                    } else {
                        Toast.makeText(this, "error hinack ni gonzales database ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}