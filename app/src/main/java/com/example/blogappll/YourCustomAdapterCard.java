package com.example.blogappll;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.blogappll.Database.CardDatabase;
import com.example.blogappll.Entity.Card;

import java.util.List;


public class YourCustomAdapterCard extends RecyclerView.Adapter<YourCustomAdapterCard.ViewHolder> {

    private List<Card> cardList;
    private CardDatabase db;  // Add CardDatabase instance
    private Context context;  // Add a Context field

    // Single constructor
    public YourCustomAdapterCard(Context context, List<Card> cardList, CardDatabase db) {
        this.cardList = cardList;
        this.context = context;  // Initialize the Context
        this.db = db;  // Initialize the database instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row6, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.title.setText(card.title);
        holder.description.setText(card.description);
        holder.amount.setText(card.amount);

        holder.deleteButton.setOnClickListener(v -> {
            // Perform deletion on a background thread
            new Thread(() -> {
                db.cardDao().delete(card); // Delete from the database

                // Update the list and notify the adapter on the main thread
                cardList.remove(position);
                ((Activity) context).runOnUiThread(() -> {  // Use context to run on the UI thread
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cardList.size());
                });
            }).start();
        });
    }


    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, amount;
        ImageButton deleteButton;  // Add the delete button reference
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recrutement_add_nom);
            description = itemView.findViewById(R.id.recrutement_add_description);
            amount = itemView.findViewById(R.id.recrutement_add_salaire);

            deleteButton = itemView.findViewById(R.id.delete_button);  // Bind the delete button
        }
    }
}