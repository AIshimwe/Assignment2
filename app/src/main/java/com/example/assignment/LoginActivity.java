package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private CheckBox rememberMeCheckBox;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        
        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        
        // Load saved credentials if Remember Me was checked
        loadSavedCredentials();
        
        // Set click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
        
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("savedEmail", "");
            String savedPassword = sharedPreferences.getString("savedPassword", "");
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }
    }
    
    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Check if fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get stored user data
        String storedEmail = sharedPreferences.getString("email", "");
        String storedPassword = sharedPreferences.getString("password", "");
        String storedName = sharedPreferences.getString("name", "");
        
        // Validate credentials
        if (email.equals(storedEmail) && password.equals(storedPassword)) {
            // Save credentials if Remember Me is checked
            if (rememberMeCheckBox.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", true);
                editor.putString("savedEmail", email);
                editor.putString("savedPassword", password);
                editor.apply();
            } else {
                // Clear saved credentials if Remember Me is unchecked
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", false);
                editor.remove("savedEmail");
                editor.remove("savedPassword");
                editor.apply();
            }
            
            Toast.makeText(this, "Login successful! Welcome back!", Toast.LENGTH_SHORT).show();
            
            // Navigate to HomeActivity with user's name
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("userName", storedName);
            intent.putExtra("userEmail", storedEmail);
            startActivity(intent);
            finish(); // Close LoginActivity
        } else {
            Toast.makeText(this, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
