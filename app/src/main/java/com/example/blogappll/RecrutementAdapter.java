package com.example.blogappll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.blogappll.Database.RecrutementDatabase;
import com.example.blogappll.Entity.Recrutement;

import java.util.List;


public class RecrutementAdapter extends RecyclerView.Adapter<RecrutementAdapter.RecrutementViewHolder> {

    private List<Recrutement> RecrutementList;
    private RecrutementDatabase db;
    private Context context;
    private RecrutementAdapter.OnUpdateClickListener onUpdateClickListener;

    // Interface pour gérer le clic sur le bouton "Update"
    public interface OnUpdateClickListener {
        void onUpdateClick(Recrutement Recrutement);
    }

    // Constructeur
    public RecrutementAdapter(Context context, List<Recrutement> RecrutementList, RecrutementDatabase db, RecrutementAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.RecrutementList = RecrutementList;
        this.db = db;
        this.context = context;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public RecrutementAdapter.RecrutementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recrutement, parent, false);
        return new RecrutementAdapter.RecrutementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecrutementAdapter.RecrutementViewHolder holder, int position) {
        // Get the current Recrutement item
        Recrutement Recrutement = RecrutementList.get(position);

        holder.nomposte.setText(Recrutement.nomposte);
        holder.description.setText(Recrutement.description);
        holder.salaire.setText(Recrutement.salaire);
        holder.localisation.setText(Recrutement.localisation);


        // Set a click listener on the entire item to open the RecrutementDetails activity
        holder.itemView.setOnClickListener(v -> {
            Log.d("RecrutementAdapter", "Item clicked: " + Recrutement.nomposte);
            Intent intent = new Intent(holder.itemView.getContext(), Recrutement_detail.class);
            intent.putExtra("nomposte", Recrutement.nomposte);
            intent.putExtra("description", Recrutement.description);
            intent.putExtra("localisation", Recrutement.localisation);
            intent.putExtra("salaire", Recrutement.salaire);
            holder.itemView.getContext().startActivity(intent);
        });

        // Handle the delete button click
        holder.deleteButton.setOnClickListener(v -> {
            Log.d("RecrutementAdapter", "Delete button clicked for: " + Recrutement.nomposte);
            deleteRecrutement(Recrutement, position);
        });

        // Handle the update button click
        holder.updateButton.setOnClickListener(v -> onUpdateClickListener.onUpdateClick(Recrutement));
    }

    // Helper method to delete a car from the database and update the RecyclerView
    private void deleteRecrutement(Recrutement Recrutement, int position) {
        new Thread(() -> {
            // Delete Recrutement from database
            db.recrutementDao().delete(Recrutement);
            RecrutementList.remove(position);

            // Update the RecyclerView on the main thread
            ((Activity) context).runOnUiThread(() -> {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, RecrutementList.size());
            });
        }).start();
    }


    public void updateRecrutementList(List<Recrutement> newRecrutementList) {
        RecrutementList.clear();
        RecrutementList.addAll(newRecrutementList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return RecrutementList.size();
    }

    // ViewHolder pour les éléments de la liste
    public static class RecrutementViewHolder extends RecyclerView.ViewHolder {
        TextView nomposte, description, salaire,localisation;

        ImageButton deleteButton, updateButton;

        public RecrutementViewHolder(View itemView) {
            super(itemView);
            nomposte = itemView.findViewById(R.id.recrutement_add_nom);
            description = itemView.findViewById(R.id.recrutement_add_description);
            salaire = itemView.findViewById(R.id.recrutement_add_salaire);
            localisation = itemView.findViewById(R.id.recrutement_add_localisation);
            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
