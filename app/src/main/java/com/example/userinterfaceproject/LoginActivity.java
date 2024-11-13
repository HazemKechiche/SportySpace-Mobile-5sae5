package com.example.userinterfaceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userinterfaceproject.db.DatabaseHelper;
import com.example.userinterfaceproject.entities.PasswordUtil;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextPersonUsername);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        databaseHelper = new DatabaseHelper(this);

        // Check if the user is already logged in (i.e., session exists)
        checkUserSession();
        findViewById(R.id.forgotPasswordLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to PasswordResetActivity
                Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkUserSession() {
        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // Get role from the database
            String role = databaseHelper.getUserRoleByUsername(username);

            if ("Admin".equals(role)) {
                // Redirect to Admin Dashboard if user is an Admin
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else if ("Joueur".equals(role)) {
                // Redirect to HomeActivity if user is a Joueur
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("username", username); // Pass the username to HomeActivity
                startActivity(intent);
            } else {
                // Handle other roles if necessary or show an error
                Toast.makeText(this, "Invalid user role", Toast.LENGTH_SHORT).show();
                return;
            }
            finish(); // Close LoginActivity so the user can't go back to it
        }
    }

    // Redirect to RegisterActivity
    public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Handle login logic
    public void onClickLogin(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Simple validation: Check if username and password are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
        } else {
            // Hash the password before validating
            String hashedPassword = PasswordUtil.hashPassword(password);
            boolean isUserValid = databaseHelper.checkUserCredentials(username, hashedPassword);
            String role = databaseHelper.getUserRole(username, hashedPassword);

            if (isUserValid) {
                // Save username in SharedPreferences (session management)
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username); // Store the username in SharedPreferences
                editor.apply();

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                if (role.equals("Admin")) {
                    // Redirect to Admin Dashboard if user is an admin
                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                } else {
                    // Redirect to HomeActivity after successful login
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("username", username); // Pass the username to HomeActivity
                    startActivity(intent);
                }
                finish(); // Close LoginActivity to prevent back navigation
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
