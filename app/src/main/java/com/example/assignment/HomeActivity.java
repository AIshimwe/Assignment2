package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TextView welcomeTextView, userEmailTextView, userGenderTextView;
    private Button logoutButton;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        
        // Initialize views
        welcomeTextView = findViewById(R.id.welcomeTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userGenderTextView = findViewById(R.id.userGenderTextView);
        logoutButton = findViewById(R.id.logoutButton);
        
        // Get user's data from intent and SharedPreferences
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userGender = sharedPreferences.getString("gender", "");
        
        // Set welcome message
        if (userName != null && !userName.isEmpty()) {
            welcomeTextView.setText("Welcome, " + userName + "!");
        } else {
            welcomeTextView.setText("Welcome, User!");
        }
        
        // Set user email
        if (userEmail != null && !userEmail.isEmpty()) {
            userEmailTextView.setText("Email: " + userEmail);
        } else {
            userEmailTextView.setText("Email: Not available");
        }
        
        // Set user gender
        if (!userGender.isEmpty()) {
            userGenderTextView.setText("Gender: " + userGender);
        } else {
            userGenderTextView.setText("Gender: Not specified");
        }
        
        // Set click listener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear remember me data
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", false);
                editor.remove("savedEmail");
                editor.remove("savedPassword");
                editor.apply();
                
                Toast.makeText(HomeActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                
                // Navigate back to LoginActivity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Close HomeActivity
            }
        });
    }
}

