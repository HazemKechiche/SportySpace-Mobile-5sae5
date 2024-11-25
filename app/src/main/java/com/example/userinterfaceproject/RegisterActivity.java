package com.example.userinterfaceproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.userinterfaceproject.db.AppDatabase;
import com.example.userinterfaceproject.db.UserDao;
import com.example.userinterfaceproject.entities.User;
import com.example.userinterfaceproject.entities.PasswordUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private static final String DEFAULT_ROLE = "Joueur";
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize EditText views
        usernameEditText = findViewById(R.id.editTextPersonUsername);
        emailEditText = findViewById(R.id.editTextTextPersonName2);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword2);

        // Initialize UserDao
        userDao = AppDatabase.getInstance(this).userDao();
    }

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

        // Create an Executor to run database queries on a background thread
        Executor executor = Executors.newSingleThreadExecutor();

        // Check if username already exists in the database (in a background thread)
        executor.execute(new Runnable() {
            @Override
            public void run() {
                User existingUser = userDao.getUserByUsername(username);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (existingUser != null) {
                            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Hash password before saving
                            String hashedPassword = PasswordUtil.hashPassword(password);

                            // Create a new User object
                            User newUser = new User(username, email, hashedPassword, DEFAULT_ROLE);

                            // Insert the new user into the database (in the background)
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    userDao.addUser(newUser);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            // Navigate to login screen
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish(); // Optional: To prevent going back to the RegisterActivity
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    // Check if the email is valid
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Check if the password meets the minimum length requirement
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    // Optional: Go to login screen without registering
    public void onClickk(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
