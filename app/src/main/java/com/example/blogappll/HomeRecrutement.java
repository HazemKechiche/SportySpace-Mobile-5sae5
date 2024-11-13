package com.example.blogappll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.blogappll.Database.RecrutementDatabase;
import com.example.blogappll.Database.RecrutementDatabase;
import com.example.blogappll.Entity.Recrutement;
import com.example.blogappll.Entity.Recrutement;

import java.util.List;

public class HomeRecrutement  extends Fragment{
    private static final String TAG = "HomeFragment";
    private RecrutementDatabase db;
    private RecyclerView recyclerView;
    private RecrutementAdapter adapter;

    public HomeRecrutement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Home fragment is created.");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_recrutement_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room database
        if (getContext() != null) {
            db = Room.databaseBuilder(getContext(), RecrutementDatabase.class, "Recrutement_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }


        // Load Recrutements from the database and update UI
        loadRecrutements();



        // Handle the "Plus" button click
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),publishrecrutement.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadRecrutements() {
        Log.d(TAG, "Fetching Recrutements from the database...");

        new Thread(() -> {
            try {
                List<Recrutement> Recrutements = db.recrutementDao().getAllRecrutements();
                Log.d(TAG, "Recrutements loaded: " + Recrutements.size());

                getActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        // Initializing adapter and setting up click listener for updating Recrutements
                        adapter = new RecrutementAdapter(getActivity(), Recrutements, db, Recrutement -> {
                            // Intent to open Publish in update mode
                            Intent intent = new Intent(getActivity(), publishrecrutement.class);
                            intent.putExtra("Recrutement_id", Recrutement.id);  // Pass Recrutement ID for updating
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateRecrutementList(Recrutements);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching Recrutements", e);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Home fragment resumed.");
        loadRecrutements();
    }
}