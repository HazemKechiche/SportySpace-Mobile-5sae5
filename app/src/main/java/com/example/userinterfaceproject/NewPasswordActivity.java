package com.example.userinterfaceproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userinterfaceproject.db.AppDatabase;
import com.example.userinterfaceproject.db.UserDao;
import com.example.userinterfaceproject.entities.PasswordUtil;
import com.example.userinterfaceproject.entities.User;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonSavePassword;
    private UserDao userDao;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSavePassword = findViewById(R.id.buttonSavePassword);

        email = getIntent().getStringExtra("email");

        // Initialize Room database and UserDao
        userDao = AppDatabase.getInstance(this).userDao();

        buttonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(NewPasswordActivity.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(NewPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    String hashedPassword = PasswordUtil.hashPassword(newPassword);
                    updatePassword(hashedPassword);
                }
            }
        });
    }

    private void updatePassword(String hashedPassword) {
        new Thread(() -> {
            User user = userDao.getUserByEmail(email);
            if (user != null) {
                user.setPassword(hashedPassword);
                userDao.updateUser(user);

                runOnUiThread(() -> {
                    Toast.makeText(NewPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(NewPasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
