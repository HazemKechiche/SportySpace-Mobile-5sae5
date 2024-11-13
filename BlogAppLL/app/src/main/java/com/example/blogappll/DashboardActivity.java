package com.example.blogappll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // The layout with the included navbar

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

    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menuButton));
        popupMenu.getMenuInflater().inflate(R.menu.bottom_nav_menu, popupMenu.getMenu());

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
             if (item.getItemId() == R.id.menu_card) {
                Log.d("PopupMenu", "card menu clicked");
                Toast.makeText(this, "Card clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, MainActivity4.class));
                return true;
            }
            return false;
        });


        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_card) {
            // Navigate to Home activity
            Intent intent = new Intent(DashboardActivity.this, MainActivity4.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
