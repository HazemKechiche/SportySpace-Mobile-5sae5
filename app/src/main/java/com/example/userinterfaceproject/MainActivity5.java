package com.example.userinterfaceproject;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5); // Your activity layout with a container for fragments

        // Check if we need to navigate to the Home fragment
        if (getIntent().getBooleanExtra("navigate_to_home", false)) {
            // Replace the current fragment with the Home fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new HomeRecrutement()) // Replace with the Home fragment
                    .commit();
        } else {
            // Default behavior: load Home fragment or any other fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new HomeRecrutement()) // Load Home fragment by default
                    .commit();
        }
    }
}
