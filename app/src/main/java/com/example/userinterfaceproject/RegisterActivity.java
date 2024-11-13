package com.example.userinterfaceproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.userinterfaceproject.db.DatabaseHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private static final String DEFAULT_ROLE = "Admin";
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize EditText views
        usernameEditText = findViewById(R.id.editTextPersonUsername);
        emailEditText = findViewById(R.id.editTextTextPersonName2);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword2);
        databaseHelper = new DatabaseHelper(this);
    }

    // Button click handler
    // Button click handler for registration
    public void onClick(View view) {
        // Get input values
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hash password before saving (use SHA-256 for hashing)
        String hashedPassword = hashPassword(password);

        // Save user data to the database
        boolean isInserted = databaseHelper.addUser(username, email, hashedPassword, DEFAULT_ROLE);

        if (isInserted) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            // Navigate to login screen
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: To prevent going back to the RegisterActivity
        } else {
            Toast.makeText(this, "Error occurred during registration", Toast.LENGTH_SHORT).show();
        }
    }

    // Check if the email is valid
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Check if the password meets the minimum length requirement
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    // Hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Optional: Go to login screen without registering
    public void onClickk(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
