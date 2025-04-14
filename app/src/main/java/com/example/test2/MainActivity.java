package com.example.test2;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Arrays;
import java.util.List;

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
	public String sem, section, name, studentNumber, gradeLevel, userID, email, gender, status;
	private FirebaseFirestore firestore;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
       firestore = FirebaseFirestore.getInstance();
        
        String currentVersion = appVersion();
        
        firestore.collection("VersionControl").document("VersionStatus").get().addOnSuccessListener(a -> {
            String updatedVersion = a.getString("version");
                
                String appLink = a.getString("link");
                if (!currentVersion.equalsIgnoreCase(updatedVersion)) {
                    ScrollView form = findViewById(R.id.form);
                    form.setVisibility(View.GONE);
                    LinearLayout display = findViewById(R.id.versionUpdate);
                    display.setVisibility(View.VISIBLE);
                    
                    TextView link = findViewById(R.id.link);
                    String htmlLink = "<a href='" + appLink + "'>" + "Install here" + "</a>";
                    link.setText(Html.fromHtml(htmlLink, Html.FROM_HTML_MODE_LEGACY));
                    link.setMovementMethod(LinkMovementMethod.getInstance());
                    
                }
        });
        
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

		
		emailEditText = findViewById(R.id.emailEditText);
		passwordEditText = findViewById(R.id.passwordEditText);
		loginButton = findViewById(R.id.loginButton);
		signupBTN = findViewById(R.id.signupBTN);
		loginStatusText = findViewById(R.id.loginStatusText);

		
		loginButton.setOnClickListener(view -> loginUser());

		signupBTN.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SignupActivity.class);
				startActivity(intent);
				overridePendingTransition(0,0);

			}
		});
        
        TextView link = findViewById(R.id.linkITM);
                    String htmlLink = "<a href='https://www.facebook.com/share/18HNuBdzEH/'>" + "Click here" + "</a>";
                    link.setText(Html.fromHtml(htmlLink, Html.FROM_HTML_MODE_LEGACY));
                    link.setMovementMethod(LinkMovementMethod.getInstance());

		
	}

	private void loginUser() {
		
	    email = emailEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();

        ScrollView form = findViewById(R.id.form);
        ProgressBar loading = findViewById(R.id.loading);
        
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		db.collection("term").document("semester").get().addOnSuccessListener(documentSnapshot -> {
			if(documentSnapshot.exists()) {
				sem = documentSnapshot.getString("sem");
				Log.d("semester : ", sem);
			}
		});
        
        form.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

		if (TextUtils.isEmpty(email)) {
			emailEditText.setError("Email is required");
           form.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			passwordEditText.setError("Password is required");
            form.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
			return;
		}
        
        firestore.collection("staff")
        .whereEqualTo("email", email)
        .whereEqualTo("password", password)
        .get()
        .addOnSuccessListener(d -> {
            if(!d.isEmpty()) {
            	getSharedPreferences("LoginPrefs", MODE_PRIVATE)
			.edit()
			.putBoolean("isStaff", true)
			.apply();

			Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
			startActivity(intent);
			overridePendingTransition(0,0);
			finish();
			overridePendingTransition(0,0);
            } else {
			firestore.collection("teachers")
			.whereEqualTo("email", email)
			.whereEqualTo("password", password)
			.get()
			.addOnSuccessListener(queryDocumentSnapshots -> {
				if (!queryDocumentSnapshots.isEmpty()) {
					DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
					String name = documentSnapshot.getString("name");
					String codename = documentSnapshot.getString("codename");
                                String status = documentSnapshot.getString("status");
					getSharedPreferences("LoginPrefs", MODE_PRIVATE)
					.edit()
					.putBoolean("isTeacher", true)
					.putString("name", name)
					.putString("email", email)
					.putString("codename", codename)
                                .putString("status", status)
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
							    name = documentSnapshot.getString("name");
							    studentNumber = documentSnapshot.getString("studentNumber");
								section = documentSnapshot.getString("section");
							    userID = documentSnapshot.getId();  // This is the userID (document name)
								 gender = documentSnapshot.getString("gender");
								 gradeLevel = documentSnapshot.getString("gradeLevel");
                                        status = documentSnapshot.getString("status");
								

								if(sem.equalsIgnoreCase("sem2")) {
									db.collection("term").document("semester").collection("section").document(section).get().addOnSuccessListener(documentSnapshot1 -> {
										if(documentSnapshot1.exists()) {
											section = documentSnapshot1.getString("section");
											studentInfo();
										} else {
											studentInfo();
										}
									});
								} else {
									studentInfo();



								}
							} else {
                                        form.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
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
        });

		 
	}

	private void studentInfo() {
		getSharedPreferences("LoginPrefs", MODE_PRIVATE)
		.edit()
		.putBoolean("isLoggedIn", true)
		.putString("name", name)
		.putString("email", email)
		.putString("studentNumber", studentNumber)
		.putString("section", section)
		.putString("userID", userID)
		.putString("gender", gender)
		.putString("gradeLevel", gradeLevel)
        .putString("status", status)
		.apply();


		Intent intent = new Intent(MainActivity.this, StudentDashboardActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("email", email);
		intent.putExtra("studentNumber", studentNumber);
		intent.putExtra("section", section);
		intent.putExtra("userID", userID);
		intent.putExtra("gender", gender);
		intent.putExtra("gradeLevel", gradeLevel);
        intent.putExtra("status", status);
		startActivity(intent);
		overridePendingTransition(0,0);

		finish();
		overridePendingTransition(0,0);
	}

	private void resetEarlyOutIfNeeded() {
		SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
		String lastResetDate = sharedPreferences.getString("lastResetDate", null);

		String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

		if (!today.equals(lastResetDate)) {
			FirebaseFirestore db = FirebaseFirestore.getInstance();
			List<String> gradeLevels = Arrays.asList("grade11", "grade12");
            
            db.collection("users").get().addOnSuccessListener(queryDocument -> {
                if (!queryDocument.isEmpty()) {
                    for (DocumentSnapshot document : queryDocument) {
                        document.getReference().update("earlyOut", false);
                    }
                }
            });
            
			for (String gradeLevel : gradeLevels) {
				db.collection("schedule").document("shs").collection(gradeLevel)
				.get()
				.addOnSuccessListener(querySnapshot -> {
					for (DocumentSnapshot sectionDoc : querySnapshot.getDocuments()) {
						// Reset the earlyOut field to false for each section
						sectionDoc.getReference().update("earlyOut", false)
						.addOnSuccessListener(aVoid -> Log.d("Firestore", "earlyOut reset to false for " + sectionDoc.getId()))
						.addOnFailureListener(e -> Log.e("Firestore", "Error resetting earlyOut for " + sectionDoc.getId(), e));
					}
				})
				.addOnFailureListener(e -> Log.e("Firestore", "Error fetching sections for " + gradeLevel, e));
			}
            
           

			// Update the last reset date in SharedPreferences
			sharedPreferences.edit().putString("lastResetDate", today).apply();
		}
	}
    
    private String appVersion() {
        String version = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
        
    }



	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, 0);
	}
}