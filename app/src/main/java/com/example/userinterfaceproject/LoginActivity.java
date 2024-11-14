package com.example.userinterfaceproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.userinterfaceproject.db.AppDatabase;
import com.example.userinterfaceproject.db.UserDao;
import com.example.userinterfaceproject.entities.PasswordUtil;
import com.example.userinterfaceproject.entities.User;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextPersonUsername);
        passwordEditText = findViewById(R.id.editTextTextPassword);

        // Get instance of UserDao
        userDao = AppDatabase.getInstance(this).userDao();

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
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // Use an Executor to run the database query on a background thread
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String role = userDao.getUserRoleByUsername(username);

                    // Switch back to the main thread to update the UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("Admin".equals(role)) {
                                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                startActivity(intent);
                                finish(); // Close LoginActivity after redirection
                            } else if ("Joueur".equals(role)) {
                                Intent intent = new Intent(LoginActivity.this, Acceuil.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish(); // Close LoginActivity after redirection
                            } else if (role == null) {
                                // If the user exists but has no role, display a message without exiting
                                Toast.makeText(LoginActivity.this, "User role not found", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle any other unexpected roles if needed
                                Toast.makeText(LoginActivity.this, "Invalid user role", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {
            // No user connected; you can display a message or leave empty if no action is needed
            Toast.makeText(this, "No user session found", Toast.LENGTH_SHORT).show();
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

            // Using Executor to perform database query on a background thread
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    User user = userDao.checkUserCredentials(username, hashedPassword); // Use hashed password for validation
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null) {
                                // Save username in SharedPreferences (session management)
                                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username); // Store the username in SharedPreferences
                                editor.apply();

                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                String role = user.getRole(); // Retrieve the role of the user

                                if ("Admin".equals(role)) {
                                    // Redirect to Admin Dashboard if user is an admin
                                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                    startActivity(intent);
                                } else if ("Joueur".equals(role)) {
                                    // Redirect to HomeActivity after successful login
                                    Intent intent = new Intent(LoginActivity.this, Acceuil.class);
                                    intent.putExtra("username", username); // Pass the username to HomeActivity
                                    startActivity(intent);
                                }

                                finish(); // Close LoginActivity to prevent back navigation
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}
