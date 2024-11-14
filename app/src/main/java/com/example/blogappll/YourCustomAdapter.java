package com.example.blogappll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.FileProvider;  // Ajout de l'import pour FileProvider

import com.example.blogappll.Database.TerrainDatabase;
import com.example.blogappll.Entity.Terrain;
import com.example.blogappll.PdfGenerator;

import java.io.File;
import java.util.List;

public class YourCustomAdapter extends RecyclerView.Adapter<YourCustomAdapter.ViewHolder> {

    private List<Terrain> TerrainList;
    private TerrainDatabase db;  // Add TerrainDatabase instance
    private Context context;  // Add a Context field

    // Single constructor
    public YourCustomAdapter(Context context, List<Terrain> TerrainList, TerrainDatabase db) {
        this.TerrainList = TerrainList;
        this.context = context;  // Initialize the Context
        this.db = db;  // Initialize the database instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Terrain terrain = TerrainList.get(position);
        holder.title.setText(terrain.title);
        holder.description.setText(terrain.description);
        holder.prix.setText(terrain.prix);

        // Set the delete button functionality
        holder.deleteButton.setOnClickListener(v -> {
            // Perform deletion on a background thread
            new Thread(() -> {
                db.TerrainDao().delete(terrain); // Delete from the database

                // Update the list and notify the adapter on the main thread
                TerrainList.remove(position);
                ((Activity) context).runOnUiThread(() -> {  // Use context to run on the UI thread
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, TerrainList.size());
                });
            }).start();
        });

        // PDF generation after adding a new terrain
        holder.itemView.setOnClickListener(v -> {
            // Generate PDF for the terrain
            File pdfFile = PdfGenerator.generateTerrainPdf(context, terrain);

            // Show a toast indicating PDF has been generated
            Toast.makeText(context, "PDF Generated: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // Check if the PDF file exists
            if (pdfFile.exists()) {
                // Create an Intent to open the PDF file
                Intent intent = new Intent(Intent.ACTION_VIEW);

                // Get URI for the PDF file using FileProvider
                Uri uri = FileProvider.getUriForFile(context, "com.example.blogappll.fileprovider", pdfFile);

                // Set the URI and MIME type for PDF
                intent.setDataAndType(uri, "application/pdf");

                // Add flags for reading permissions
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Start the activity to view the PDF
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return TerrainList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, prix;
        ImageButton deleteButton;  // Add the delete button reference
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.b_title);
            description = itemView.findViewById(R.id.btn_desc);
            prix = itemView.findViewById(R.id.btn_auth);
            deleteButton = itemView.findViewById(R.id.delete_button);  // Bind the delete button
        }
    }
}
