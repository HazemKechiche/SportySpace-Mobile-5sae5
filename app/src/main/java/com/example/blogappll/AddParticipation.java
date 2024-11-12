package com.example.blogappll;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddParticipation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participation);

        // References to form fields
        EditText nomequipe = findViewById(R.id.nomequipe);
        EditText leadername = findViewById(R.id.leadername);
        EditText nombremembres = findViewById(R.id.nombremembres);

        // Handle form submission
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the form values
                String equipe = nomequipe.getText().toString();
                String leader = leadername.getText().toString();
                String membres = nombremembres.getText().toString();

                // Simple validation
                if (equipe.isEmpty() || leader.isEmpty() || membres.isEmpty()) {
                    Toast.makeText(AddParticipation.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Show a message or process the data (e.g., save to database)
                    Toast.makeText(AddParticipation.this, "Participation added successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate back to TournoiDetails or another screen
                    finish();
                }
            }
        });
    }
}
