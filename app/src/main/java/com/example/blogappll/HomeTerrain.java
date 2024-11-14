package com.example.blogappll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.blogappll.Database.TerrainDatabase;
import com.example.blogappll.Entity.Terrain;

import java.io.File;
import java.util.List;

public class HomeTerrain extends Fragment {

    private static final String TAG = "HomeFragment";
    private TerrainDatabase db;
    private RecyclerView recyclerView;
    private TerrainAdapter adapter;

    public HomeTerrain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Home fragment is created.");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room database
        db = Room.databaseBuilder(getContext(), TerrainDatabase.class, "Terrain_database")
                .fallbackToDestructiveMigration()
                .build();

        // Load Terrain from the database and update UI
        loadTerrains();

        // Find the "Plus" button and set the click listener
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Publish.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadTerrains() {
        Log.d(TAG, "Fetching Terrain from the database...");

        new Thread(() -> {
            try {
                List<Terrain> Terrains = db.TerrainDao().getAllTerrains();
                Log.d(TAG, "Terrain loaded: " + Terrains.size());

                // Generate PDF for the last added terrain if it exists
                if (!Terrains.isEmpty()) {
                    Terrain lastAddedTerrain = Terrains.get(Terrains.size() - 1);
                    File pdfFile = PdfGenerator.generateTerrainPdf(getContext(), lastAddedTerrain);

                    // Optional: Show a toast message confirming PDF generation
                    if (pdfFile != null) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "PDF created: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show()
                        );
                    }
                }

                getActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        // Initialize the adapter and set update click handling
                        adapter = new TerrainAdapter(getActivity(), Terrains, db, Terrain -> {
                            Intent intent = new Intent(getActivity(), Publish.class);
                            intent.putExtra("Terrain_id", Terrain.id);  // Pass the Terrain ID for update
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateTerrainList(Terrains);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching Terrains", e);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Home fragment resumed.");
        loadTerrains();
    }
}
