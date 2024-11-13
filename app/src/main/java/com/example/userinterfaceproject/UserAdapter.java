package com.example.userinterfaceproject;
// UserAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.userinterfaceproject.db.DatabaseHelper;
import com.example.userinterfaceproject.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private DatabaseHelper databaseHelper;
    private Context context;
    private AdminDashboardActivity adminDashboardActivity; // Pass AdminDashboardActivity to refresh user list


    public UserAdapter(List<User> userList, DatabaseHelper databaseHelper, Context context) {
        this.userList = userList;
        this.databaseHelper = databaseHelper;
        this.context = context;
        this.adminDashboardActivity = adminDashboardActivity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.roleTextView.setText(user.getRole());
        holder.EmailTextView.setText(user.getEmail());



        // Delete user on button click
        holder.deleteUserButton.setOnClickListener(v -> {
            databaseHelper.deleteUserById(user.getId());
            userList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, userList.size());

            Toast.makeText(adminDashboardActivity, "User deleted", Toast.LENGTH_SHORT).show();
            adminDashboardActivity.refreshUserList();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateUserList(List<User> newUserList) {
        userList = newUserList;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, roleTextView,EmailTextView;

        Button deleteUserButton;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            EmailTextView = itemView.findViewById(R.id.EmailTextView);
            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
        }
    }
}
