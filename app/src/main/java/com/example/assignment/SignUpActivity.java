package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton, otherRadioButton;
    private CheckBox termsCheckBox;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        
        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        otherRadioButton = findViewById(R.id.otherRadioButton);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        
        // Set click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });
    }
    
    private void performSignUp() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Check if fields are empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if email is valid (basic validation)
        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if password is at least 6 characters
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if gender is selected
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check if terms are accepted
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get selected gender
        String gender = "";
        if (selectedGenderId == R.id.maleRadioButton) {
            gender = "Male";
        } else if (selectedGenderId == R.id.femaleRadioButton) {
            gender = "Female";
        } else if (selectedGenderId == R.id.otherRadioButton) {
            gender = "Other";
        }
        
        // Save user data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("gender", gender);
        editor.putBoolean("termsAccepted", true);
        editor.apply();
        
        // Show success message
        Toast.makeText(this, "Account created successfully! Welcome " + name + "!", Toast.LENGTH_LONG).show();
        
        // Navigate back to LoginActivity
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close SignUpActivity
    }
}

