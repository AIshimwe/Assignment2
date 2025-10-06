package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.Model.StudentClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private ListView userListView;
    private android.widget.TextView emptyStateTextView;
    private List<StudentClass> userList;
    private UserListAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Initialize views
        userListView = findViewById(R.id.userListView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);

        // Load users from SharedPreferences
        loadUsers();

        // Set up adapter
        adapter = new UserListAdapter(this, userList);
        userListView.setAdapter(adapter);
        updateEmptyState();

        // Keep item click to also allow tapping row to open details
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudentClass selectedUser = userList.get(position);
                openUserDetails(selectedUser);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload list in case new users were added from another session
        loadUsers();
        if (adapter != null) {
            adapter = new UserListAdapter(this, userList);
            userListView.setAdapter(adapter);
        }
        updateEmptyState();
    }

    private void loadUsers() {
        userList = new ArrayList<>();
        
        try {
            // Prefer list storage if available
            String serializedList = sharedPreferences.getString("studentList", "");
            if (serializedList != null && !serializedList.isEmpty()) {
                List<StudentClass> fromList = deserializeStudentList(serializedList);
                if (fromList != null) {
                    userList.addAll(fromList);
                }
            } else {
                // Backward compatibility: single stored student
                String serializedStudent = sharedPreferences.getString("studentObject", "");
                if (!serializedStudent.isEmpty()) {
                    StudentClass student = deserializeStudent(serializedStudent);
                    userList.add(student);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(this, "Error loading user data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyState() {
        if (userList == null || userList.isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
            userListView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);
            userListView.setVisibility(View.VISIBLE);
        }
    }

    // Helper method to deserialize StudentClass object
    private StudentClass deserializeStudent(String serializedStudent) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(serializedStudent, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        StudentClass student = (StudentClass) ois.readObject();
        ois.close();
        return student;
    }

    @SuppressWarnings("unchecked")
    private List<StudentClass> deserializeStudentList(String serializedList) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(serializedList, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<StudentClass> students = (List<StudentClass>) ois.readObject();
        ois.close();
        return students;
    }

    private void openUserDetails(StudentClass user) {
        Intent intent = new Intent(UserListActivity.this, UserDetailsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}

