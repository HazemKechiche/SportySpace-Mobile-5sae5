package com.example.userinterfaceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userinterfaceproject.db.UserDao;
import com.example.userinterfaceproject.entities.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private UserDao userDao;
    private Context context;
    private AdminDashboardActivity adminDashboardActivity; // Pass AdminDashboardActivity to refresh user list

    public UserAdapter(List<User> userList, UserDao userDao, AdminDashboardActivity adminDashboardActivity) {
        this.userList = userList;
        this.userDao = userDao;
        this.adminDashboardActivity = adminDashboardActivity;
        this.context = adminDashboardActivity;
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
        holder.emailTextView.setText(user.getEmail());

        // Delete user on button click
        holder.deleteUserButton.setOnClickListener(v -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                userDao.deleteUser(user);  // Perform deletion in background

                userList.remove(position);
                // Refresh the UI on the main thread
                adminDashboardActivity.runOnUiThread(() -> {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, userList.size());
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                    adminDashboardActivity.refreshUserList(); // Refresh user list
                });
            });
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
        TextView usernameTextView, roleTextView, emailTextView;
        Button deleteUserButton;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            emailTextView = itemView.findViewById(R.id.EmailTextView);
            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
        }
    }
}
