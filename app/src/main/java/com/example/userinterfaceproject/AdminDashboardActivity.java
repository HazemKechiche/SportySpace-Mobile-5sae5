// AdminDashboardActivity.java
package com.example.userinterfaceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.userinterfaceproject.db.AppDatabase;
import com.example.userinterfaceproject.db.UserDao;
import com.example.userinterfaceproject.entities.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView userRecyclerView;
    private UserDao userDao;
    private UserAdapter userAdapter;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize the Room database and UserDao
        userDao = AppDatabase.getInstance(this).userDao();

        // Set up RecyclerView
        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up logout button
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Load and display users
        loadUserList();
    }


    private void loadUserList() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch users from the UserDao in the background
                List<User> users = userDao.getAllUsers();

                // Update the RecyclerView adapter on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Ensure userAdapter is only created once and updated with new data
                        if (userAdapter == null) {
                            userAdapter = new UserAdapter(users, userDao, AdminDashboardActivity.this);
                            userRecyclerView.setAdapter(userAdapter);
                        } else {
                            userAdapter.updateUserList(users);
                        }
                    }
                });
            }
        });
    }

    private void logout() {
        // Clear session or shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login screen
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void refreshUserList() {
        loadUserList();  // Reloads the user list by calling loadUserList
    }
}
