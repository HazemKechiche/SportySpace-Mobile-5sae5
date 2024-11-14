package com.example.blogappll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogappll.Database.TerrainDatabase;
import com.example.blogappll.Entity.Terrain;

import java.util.List;

public class TerrainAdapter extends RecyclerView.Adapter<TerrainAdapter.TerrainViewHolder> {

    private List<Terrain> TerrainList;
    private TerrainDatabase db;
    private Context context;
    private OnUpdateClickListener onUpdateClickListener;

    // Interface pour gérer le clic sur le bouton "Update"
    public interface OnUpdateClickListener {
        void onUpdateClick(Terrain Terrain);
    }

    // Constructeur
    public TerrainAdapter(Context context, List<Terrain> TerrainList, TerrainDatabase db, OnUpdateClickListener onUpdateClickListener) {
        this.TerrainList = TerrainList;
        this.db = db;
        this.context = context;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public TerrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        return new TerrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TerrainViewHolder holder, int position) {
        Terrain Terrain = TerrainList.get(position);

        holder.title.setText(Terrain.title);
        holder.description.setText(Terrain.description);
        holder.prix.setText(Terrain.prix);

        if (Terrain.imageUri != null && !Terrain.imageUri.isEmpty()) {
            Uri imageUri = Uri.parse(Terrain.imageUri);
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            Log.d("TerrainAdapter", "Item clicked: " + Terrain.title);
            Intent intent = new Intent(holder.itemView.getContext(), TerrainDetails.class);
            intent.putExtra("title", Terrain.title);
            intent.putExtra("description", Terrain.description);
            intent.putExtra("prix", Terrain.prix);
            intent.putExtra("imageUri", Terrain.imageUri);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            Log.d("TerrainAdapter", "Delete button clicked for: " + Terrain.title);
            deleteTerrain(Terrain, position);
        });

        // Update button now launches PublishActivity for updating
        holder.updateButton.setOnClickListener(v -> {
            Intent updateIntent = new Intent(holder.itemView.getContext(), Publish.class);
            updateIntent.putExtra("Terrain_id", Terrain.id); // Pass the Terrain ID
            holder.itemView.getContext().startActivity(updateIntent);
        });
    }

    // Helper method to delete a Terrain from the database and update the RecyclerView
    private void deleteTerrain(Terrain Terrain, int position) {
        new Thread(() -> {
            // Delete Terrain from database
            db.TerrainDao().delete(Terrain);
            TerrainList.remove(position);

            // Update the RecyclerView on the main thread
            ((Activity) context).runOnUiThread(() -> {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, TerrainList.size());
            });
        }).start();
    }


    public void updateTerrainList(List<Terrain> newTerrainList) {
        TerrainList.clear();
        TerrainList.addAll(newTerrainList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return TerrainList.size();
    }

    // ViewHolder pour les éléments de la liste
    public static class TerrainViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, prix;
        ImageView image;
        ImageButton deleteButton, updateButton;

        public TerrainViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.b_title);
            description = itemView.findViewById(R.id.btn_desc);
            prix = itemView.findViewById(R.id.btn_auth);
            image = itemView.findViewById(R.id.image_tmb);
            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
