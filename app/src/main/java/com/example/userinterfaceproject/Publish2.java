package com.example.userinterfaceproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;


import com.example.userinterfaceproject.db.TerrainDatabase;
import com.example.userinterfaceproject.entities.Terrain;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Publish2 extends AppCompatActivity {

    private TerrainDatabase db;
    private Uri imageUri;
    private ImageView imageView;
    private int TerrainId = -1;  // Utilisé pour identifier si on est en mode ajout ou mise à jour

    // Declare constants
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_SELECT_IMAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish2);
        db = Room.databaseBuilder(getApplicationContext(), TerrainDatabase.class, "Terrain_database")
                .allowMainThreadQueries()  // Pour les tests uniquement
                .build();

        imageView = findViewById(R.id.image_tmb);
        EditText titleField = findViewById(R.id.b_title);
        EditText descField = findViewById(R.id.btn_desc);
        EditText prixField = findViewById(R.id.btn_auth);

        // Get reference to the back arrow ImageView
        ImageView backArrow = findViewById(R.id.back_arrow3);  // Assuming you added the ImageView for the back arrow in XML

        // Set click listener for the back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();  // This will take the user back to the previous activity or fragment
            }
        });

        // Vérifier si un Terrain_id est passé pour la mise à jour
        Intent intent = getIntent();
        if (intent.hasExtra("Terrain_id")) {
            TerrainId = intent.getIntExtra("Terrain_id", -1);
            if (TerrainId != -1) {
                Log.d("PublishActivity", "TerrainId received: " + TerrainId);
                loadTerrainData(TerrainId, titleField, descField, prixField);
            } else {
                Log.e("PublishActivity", "Invalid TerrainId received");
            }
        } else {
            Log.e("PublishActivity", "No Terrain_id found in intent");
        }

        Button btnSelectImage = findViewById(R.id.b_selectimage);
        btnSelectImage.setOnClickListener(v -> checkPermissionsAndSelectImage());

        // Gérer l'action du bouton publier
        Button btnPublish = findViewById(R.id.btn_pub);
        btnPublish.setText(TerrainId != -1 ? "UPDATE" : "PUBLISH");  // Changer le texte du bouton en fonction du mode
        btnPublish.setOnClickListener(v -> {
            if (validateFields()) {  // Vérifier la validité du formulaire avant de publier
                if (TerrainId != -1) {
                    updateTerrain(titleField.getText().toString(), descField.getText().toString(), prixField.getText().toString());
                } else {
                    insertTerrain(titleField.getText().toString(), descField.getText().toString(), prixField.getText().toString());
                }
                returnToHome();  // Retourner à l'écran d'accueil
            }
        });
    }

    private void checkPermissionsAndSelectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSIONS);
            } else {
                selectImage();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
            } else {
                selectImage();
            }
        } else {
            selectImage(); // Pas de permissions nécessaires
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(this, "Permission denied. Cannot select images.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
                try {
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadTerrainData(int id, EditText titleField, EditText descField, EditText prixField) {
        Terrain Terrain = db.TerrainDao().getTerrainById(id);
        if (Terrain != null) {
            titleField.setText(Terrain.title);
            descField.setText(Terrain.description);
            prixField.setText(Terrain.prix);
            if (Terrain.imageUri != null) {
                imageUri = Uri.parse(Terrain.imageUri);
                imageView.setImageURI(imageUri);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            Log.e("PublishActivity", "Failed to load Terrain with id: " + id);
            Toast.makeText(this, "Failed to load event data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertTerrain(String title, String description, String prix) {
        Terrain Terrain = new Terrain();
        Terrain.title = title;
        Terrain.description = description;
        Terrain.prix = prix;
        Terrain.imageUri = imageUri != null ? imageUri.toString() : null;
        db.TerrainDao().insert(Terrain);
        Toast.makeText(this, "Terrain published successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateTerrain(String title, String description, String prix) {
        Terrain Terrain = db.TerrainDao().getTerrainById(TerrainId);
        if (Terrain != null) {
            Terrain.title = title;
            Terrain.description = description;
            Terrain.prix = prix;
            if (imageUri != null) {
                Terrain.imageUri = imageUri.toString();
            }
            db.TerrainDao().update(Terrain);
            Toast.makeText(this, "Terrain updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        EditText titleField = findViewById(R.id.b_title);
        EditText descField = findViewById(R.id.btn_desc);
        EditText prixField = findViewById(R.id.btn_auth);

        if (titleField.getText().toString().isEmpty()) {
            titleField.setError("Title is required");
            return false;
        }
        if (descField.getText().toString().isEmpty()) {
            descField.setError("Description is required");
            return false;
        }
        if (prixField.getText().toString().isEmpty()) {
            prixField.setError("prix is required");
            return false;
        }
        return true;
    }

    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
