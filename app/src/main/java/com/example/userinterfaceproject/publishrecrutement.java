package com.example.userinterfaceproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.userinterfaceproject.db.RecrutementDatabase;
import com.example.userinterfaceproject.entities.Recrutement;


public class publishrecrutement extends AppCompatActivity {

    private RecrutementDatabase db;
    private int RecrutementId  = -1;  // Utilisé pour identifier si on est en mode ajout ou mise à jour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_recrutement);
        db = Room.databaseBuilder(getApplicationContext(), RecrutementDatabase.class, "Recrutement_database")
                .allowMainThreadQueries()  // Pour les tests uniquement
                .build();

        EditText nomposte = findViewById(R.id.recrutement_add_nom);
        EditText description = findViewById(R.id.recrutement_add_description);
        EditText salaire = findViewById(R.id.recrutement_add_salaire);
        EditText localisation = findViewById(R.id.recrutement_add_localisation);

        // Set click listener for the back arrow
        ImageView backArrow = findViewById(R.id.back_arrow); // Change 'ImageView' to the correct type if it's not an ImageView
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();  // This will take the user back to the previous activity or fragment
            }
        });

        // Vérifier si un Recrutement_id est passé pour la mise à jour
        Intent intent = getIntent();
        if (intent.hasExtra("Recrutement_id")) {
            RecrutementId = intent.getIntExtra("Recrutement_id", -1);
            if (RecrutementId  != -1) {
                Log.d("RecrutementActivity", "RecrutementId received: " + RecrutementId);
                loadRecrutementData(RecrutementId, nomposte, description, salaire, localisation);
            } else {
                Log.e("PublishActivity", "Invalid RecrutementId received");
            }
        } else {
            Log.e("PublishActivity", "No recrutement_id found in intent");
        }

        // Gérer l'action du bouton publier
        Button btnPublish = findViewById(R.id.btn_pub);
        btnPublish.setText(RecrutementId != -1 ? "UPDATE" : "PUBLISH");  // Changer le texte du bouton en fonction du mode
        btnPublish.setOnClickListener(v -> {
            if (validateFields()) {  // Vérifier la validité du formulaire avant de publier
                if (RecrutementId  != -1) {
                    updateRecrutement(nomposte.getText().toString(), description.getText().toString(), salaire.getText().toString(), localisation.getText().toString());
                } else {
                    insertRecrutement(nomposte.getText().toString(), description.getText().toString(), salaire.getText().toString(), localisation.getText().toString());
                }
                returnToHome();  // Retourner à l'écran d'accueil
            }
        });
    }

    private void loadRecrutementData(int id, EditText nompostefield, EditText descField, EditText salaireField, EditText localisationField) {
        Recrutement Recrutement = db.recrutementDao().getRecrutementById(id);
        if (Recrutement != null) {
            nompostefield.setText(Recrutement.nomposte);
            descField.setText(Recrutement.description);
            salaireField.setText(Recrutement.salaire);
            localisationField.setText(Recrutement.localisation);
        } else {
            Log.e("PublishActivity", "Failed to load recrutement with id: " + id);
            Toast.makeText(this, "Failed to load event data.", Toast.LENGTH_SHORT).show();
        }
    }


    private void insertRecrutement(String nomposte, String description, String salaire, String localisation) {
        Recrutement Recrutement = new Recrutement();
        Recrutement.nomposte = nomposte;
        Recrutement.description = description;
        Recrutement.salaire = salaire;
        Recrutement.localisation = localisation;

        db.recrutementDao().insert(Recrutement);
        Toast.makeText(this, "Recrutement published successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateRecrutement(String nomposte, String description, String salaire, String localisation) {
        Recrutement Recrutement = db.recrutementDao().getRecrutementById(RecrutementId);
        if (Recrutement != null) {
            Recrutement.nomposte = nomposte;
            Recrutement.description = description;
            Recrutement.salaire = salaire;
            Recrutement.localisation = localisation;

            db.recrutementDao().update(Recrutement);
            Toast.makeText(this, "Recrutement updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        EditText nomposteField = findViewById(R.id.recrutement_add_nom);
        EditText descriptionField = findViewById(R.id.recrutement_add_description);
        EditText salaireField = findViewById(R.id.recrutement_add_salaire);
        EditText localisationField = findViewById(R.id.recrutement_add_localisation);

        if (nomposteField.getText().toString().isEmpty()) {
            nomposteField.setError("name is required");
            return false;
        }
        if (descriptionField.getText().toString().isEmpty()) {
            descriptionField.setError("Description is required");
            return false;
        }
        if (salaireField.getText().toString().isEmpty()) {
            salaireField.setError("salaire is required");
            return false;
        }
        if (localisationField.getText().toString().isEmpty()) {
            localisationField.setError("localisation is required");
            return false;
        }
        return true;
    }

    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity5.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
