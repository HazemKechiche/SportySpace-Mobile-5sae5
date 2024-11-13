package com.example.userinterfaceproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userinterfaceproject.db.DatabaseHelper;
import com.example.userinterfaceproject.entities.PasswordUtil;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonSavePassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSavePassword = findViewById(R.id.buttonSavePassword);
        String email = getIntent().getStringExtra("email");

        databaseHelper = new DatabaseHelper(this);

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

                    // Save the new hashed password in the database
                    // Here you would need the user’s email or username to identify the account
                    // Assuming we pass it from the previous activities
                    databaseHelper.updateUserPassword(email, hashedPassword);

                    Toast.makeText(NewPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to login screen
                    Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
