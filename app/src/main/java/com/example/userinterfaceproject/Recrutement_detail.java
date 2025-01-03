package com.example.userinterfaceproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Recrutement_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recrutement_details);


        // Get references to the UI components
        TextView nomposte = findViewById(R.id.recrutement_details_nomposte);
        TextView localisation = findViewById(R.id.recrutement_details_localisation);
        TextView salaire = findViewById(R.id.recrutement_details_salaire);
        TextView description = findViewById(R.id.recrutement_details_description);

        ImageView backArrow = findViewById(R.id.back_arrow); // Change 'ImageView' to the correct type if it's not an ImageView
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();  // This will take the user back to the previous activity or fragment
            }

        });

        // Receive data from the intent
        Intent intent = getIntent();
        String Recrutementnomposte = intent.getStringExtra("nomposte");
        String Recrutementsalaire = intent.getStringExtra("salaire");
        String RecrutementDescription = intent.getStringExtra("description");
        String Recrutementlocalisation = intent.getStringExtra("localisation");


        // Set the data in UI components
        nomposte.setText(Recrutementnomposte != null ? Recrutementnomposte : "No offername");
        description.setText(RecrutementDescription != null ? RecrutementDescription : "No Description");
        salaire.setText(Recrutementsalaire != null ? Recrutementsalaire : "No salaire");
        localisation.setText(Recrutementlocalisation != null ? Recrutementlocalisation : "No localisation");

        // Set click listener on localisation TextView to open Google Maps
        localisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Recrutementlocalisation != null && !Recrutementlocalisation.isEmpty()) {
                    // Create a URI for the location and start the Maps intent
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(Recrutementlocalisation));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    // Verify that the device has a Maps app installed
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(Recrutement_detail.this, "No map application found", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Find the "Participate" button and set the click listener
        Button participateButton = findViewById(R.id.btn_postuler); // Make sure this ID matches your layout
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recrutement_detail.this, AddParticipation.class);

                // Pass data to the AddParticipation activity
                intent.putExtra("nomposte", Recrutementnomposte);
                intent.putExtra("description", RecrutementDescription);
                intent.putExtra("salaire", Recrutementsalaire);
                intent.putExtra("localisation", Recrutementlocalisation);

                startActivity(intent);
            }
        });
    }
}