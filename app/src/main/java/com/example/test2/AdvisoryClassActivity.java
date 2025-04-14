package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdvisoryClassActivity extends AppCompatActivity {
	private TextView logout;
	AdvisoryClassAdapter advisoryClassAdapter;
	private ProgressBar progressBar;
	static String sem;
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advisory_class);

		db.collection("term").document("semester").get().addOnSuccessListener(documentSnapshot -> {
			if(documentSnapshot.exists()) {
				sem = documentSnapshot.getString("sem");
				classAdvisory();
			}
		});

		logout = findViewById(R.id.logout);
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

	public void classAdvisory() {
		String advisor = getIntent().getStringExtra("advisor");
        
		List<Section> sections = new ArrayList<>();
		progressBar = findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		RecyclerView recyclerView = findViewById(R.id.recyclerView);

		db.collection("schedule")
		.document("shs")
		.collection("grade12")
		.whereEqualTo("advisor", advisor)
		.whereEqualTo("sem", sem)
		.get()
		.addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				for (QueryDocumentSnapshot document : task.getResult()) {
					String semVal = document.getString("sem");
					Section section = new Section(document.getId(), "grade12");
					sections.add(section);
				}

				
						progressBar.setVisibility(View.GONE);
						recyclerView.setVisibility(View.VISIBLE);
						recyclerView.setLayoutManager(new LinearLayoutManager(this));
						advisoryClassAdapter = new AdvisoryClassAdapter(this, sections);
						recyclerView.setAdapter(advisoryClassAdapter);
			}
		});
        
        db.collection("schedule")
		.document("shs")
		.collection("grade11")
		.whereEqualTo("advisor", advisor)
		.whereEqualTo("sem", sem)
		.get()
		.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
				for (QueryDocumentSnapshot document1 : task1.getResult()) {
					String semVal = document1.getString("sem");
					Section section = new Section(document1.getId(), "grade11");
					sections.add(section);
				}
                    progressBar.setVisibility(View.GONE);
						recyclerView.setVisibility(View.VISIBLE);
						recyclerView.setLayoutManager(new LinearLayoutManager(this));
						advisoryClassAdapter = new AdvisoryClassAdapter(this, sections);
						recyclerView.setAdapter(advisoryClassAdapter);
        };
        

	});
        }
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, 0);
	}
}