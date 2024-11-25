package com.example.userinterfaceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Acceuil extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil); // The layout with the included navbar

        // Access the navbar elements
        ImageView backArrowButton = findViewById(R.id.backArrowButton);
        TextView navbarTitle = findViewById(R.id.navbarTitle);
        ImageView menuButton = findViewById(R.id.menuButton);

        // Set up the back button action
        backArrowButton.setOnClickListener(v -> finish()); // Navigates back to the previous screen

        // Customize the navbar title
        navbarTitle.setText("Dashboard"); // Customize the title for the specific page

        // Set up the menu button action
        menuButton.setOnClickListener(v -> showMenu()); // Opens the menu when clicked
        // Initialize UI components
        welcomeMessage = findViewById(R.id.welcomeMessage);
        logoutButton = findViewById(R.id.logoutButton);

        // Get username from the intent (you need to pass it during login)
        String username = getIntent().getStringExtra("username");

        // Set welcome message
        welcomeMessage.setText("Welcome, " + username + "!");

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
                Intent intent = new Intent(Acceuil.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the HomeActivity so the user can't go back to it
            }
        });

    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menuButton));
        popupMenu.getMenuInflater().inflate(R.menu.dashboard_menu, popupMenu.getMenu());

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_tournoi) {
                Log.d("PopupMenu", "Tournoi menu clicked");
                Toast.makeText(this, "Tournoi clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Acceuil.this, MainActivity.class));
                return true;
            }else if (item.getItemId() == R.id.menu_card) {
                Log.d("PopupMenu", "card menu clicked");
                Toast.makeText(this, "Card clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Acceuil.this, MainActivity4.class));
                return true;
            }else if (item.getItemId() == R.id.menu_terrain) {
                Log.d("PopupMenu", "terrain menu clicked");
                Toast.makeText(this, "Terrain clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Acceuil.this, MainActivity2.class));
                return true;
            }else if (item.getItemId() == R.id.menu_recrutement) {
                Log.d("PopupMenu", "recrutement menu clicked");
                Toast.makeText(this, "Recrutement clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Acceuil.this, MainActivity5.class));
                return true;
            }else if (item.getItemId() == R.id.menu_profile) {
                Log.d("PopupMenu", "edit profile clicked");
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Acceuil.this, ProfileActivity.class));
                return true;
            }
            return false;
        });


        popupMenu.show();
    }



}