package com.example.assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.Model.StudentClass;

public class UserDetailsActivity extends AppCompatActivity {
    private TextView nameTextView, studentIdTextView, emailTextView, 
                     phoneTextView, genderTextView, passwordTextView;
    private StudentClass user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Get user object from intent
        user = (StudentClass) getIntent().getSerializableExtra("user");

        // Initialize views
        initializeViews();

        // Display user details
        if (user != null) {
            displayUserDetails();
        }
    }

    private void initializeViews() {
        nameTextView = findViewById(R.id.nameTextView);
        studentIdTextView = findViewById(R.id.studentIdTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        genderTextView = findViewById(R.id.genderTextView);
        // Optional: show password for assignment completeness
        int passwordId = getResources().getIdentifier("passwordTextView", "id", getPackageName());
        if (passwordId != 0) {
            passwordTextView = findViewById(passwordId);
        }
    }

    private void displayUserDetails() {
        nameTextView.setText(user.getName());
        studentIdTextView.setText(user.getStudentId());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhoneNumber());
        genderTextView.setText(user.getGender());
        if (passwordTextView != null) {
            passwordTextView.setText(user.getPassword());
        }

        // Clickable Email -> open email app
        emailTextView.setOnClickListener(v -> {
            String email = user.getEmail();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email });
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello " + user.getName());
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });

        // Clickable Phone -> open dialer
        phoneTextView.setOnClickListener(v -> {
            String phone = user.getPhoneNumber();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        });
    }
}

