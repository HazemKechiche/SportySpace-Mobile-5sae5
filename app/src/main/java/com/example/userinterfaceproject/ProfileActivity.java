package com.example.userinterfaceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.userinterfaceproject.db.AppDatabase;
import com.example.userinterfaceproject.db.UserDao;
import com.example.userinterfaceproject.entities.PasswordUtil;
import com.example.userinterfaceproject.entities.User;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button buttonEdit, buttonSave;

    private UserDao userDao;  // Initialize with Room's database builder
    //private int userId = 1;   // Replace with the actual user ID for the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Initialize Room Database and UserDao
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "UserDatabase.db").allowMainThreadQueries().build();
        userDao = db.userDao();
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id",0);
        String username = sharedPreferences.getString("username",null);
        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        User user = userDao.getUserByUsername(username);
        editTextUsername.setText(user.getUsername());
        editTextEmail.setText(user.getEmail());

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonSave = findViewById(R.id.buttonSave);

        // Load user data
        loadUserData(username);

        // Toggle edit mode when "Edit" button is clicked
        buttonEdit.setOnClickListener(v -> enableEditMode(true));

        // Save updated data when "Save" button is clicked
        buttonSave.setOnClickListener(v -> {
            updateUserData(username);
            enableEditMode(false);
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData(String userId) {
        User user = userDao.getUserByUsername(userId); // Fetch user data
        if (user != null) {
            editTextUsername.setText(user.getUsername());
            editTextEmail.setText(user.getEmail());
            //editTextPassword.setText(user.getPassword()); // Ideally, show as masked or hint
        }
    }

    private void enableEditMode(boolean enable) {
        editTextUsername.setEnabled(enable);
        editTextEmail.setEnabled(enable);
        editTextPassword.setEnabled(enable);
        buttonEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
        buttonSave.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void updateUserData(String userId) {
        String newUsername = editTextUsername.getText().toString();
        String newEmail = editTextEmail.getText().toString();
        String newPassword = editTextPassword.getText().toString();

        // Retrieve the current user and update fields
        User user = userDao.getUserByUsername(userId);
        if (user != null) {
            user.setUsername(newUsername);
            user.setEmail(newEmail);
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setPassword(hashedPassword);  // Hash if needed
            userDao.updateUser(user);       // Save changes to the DB
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
        }
    }
}
