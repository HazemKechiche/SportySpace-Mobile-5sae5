package com.example.userinterfaceproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userinterfaceproject.entities.Mailing;

import java.util.Random;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private Mailing mailing;
    private String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        // Initialize UI components
        emailEditText = findViewById(R.id.editTextEmail);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        mailing = new Mailing();


        // Handle password reset button click
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if (!email.isEmpty()) {
                    verificationCode = generateVerificationCode(); // Generate code
                    mailing.sendVerificationEmail(PasswordResetActivity.this, email, verificationCode);
                    Intent intent = new Intent(PasswordResetActivity.this, VerifyCodeActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", verificationCode); // Pass the code to HomeActivity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PasswordResetActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to simulate sending password reset link (backend integration needed)
//    private void sendPasswordResetLink(String email) {
//        // Example: Simulating backend interaction
//        Toast.makeText(this, "Password reset link sent to: " + email, Toast.LENGTH_SHORT).show();
//
//        // Redirect user to login screen after resetting password
//        finish();
//    }
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates a 6-digit code
        return String.valueOf(code);
    }
}
