// AdminDashboardActivity.java
package com.example.userinterfaceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.userinterfaceproject.db.DatabaseHelper;
import com.example.userinterfaceproject.entities.User;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView userRecyclerView;
    private DatabaseHelper databaseHelper;
    private UserAdapter userAdapter;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        logoutButton = findViewById(R.id.logoutButton);

        userRecyclerView = findViewById(R.id.userRecyclerView);
        databaseHelper = new DatabaseHelper(this);

        // Fetch and display the list of users
        List<User> userList = databaseHelper.getAllUsers();
        userAdapter = new UserAdapter(userList, databaseHelper, this);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(userAdapter);
        // Handle logout button click
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out logic: Clear session or shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all data from SharedPreferences (logout)
                editor.apply();

                // Redirect to login screen
                Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the HomeActivity so the user can't go back to it
            }
        });
    }

    public void refreshUserList() {
        List<User> userList = databaseHelper.getAllUsers();
        userAdapter.updateUserList(userList);
    }
}
