package com.example.userinterfaceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerifyCodeActivity extends AppCompatActivity {

    private EditText editTextVerificationCode;
    private Button buttonVerifyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        buttonVerifyCode = findViewById(R.id.buttonVerifyCode);
        String code = getIntent().getStringExtra("code");
        String email = getIntent().getStringExtra("email");


        buttonVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredCode = editTextVerificationCode.getText().toString();

                // Retrieve the saved verification code from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                String savedCode = sharedPreferences.getString("code", "");
                if (code == null) {
                    // Log or handle the error if the code is missing
                    Log.e("VerifyCodeActivity", "Verification code not passed in intent");
                } else {
                    Log.d("VerifyCodeActivity", "Received code: " + code);
                    // Use the verification code as needed
                }
                // Check if entered code matches the saved code
                if (enteredCode.equals(code)) {
                    // Code is correct, proceed to reset password screen
                    Intent intent = new Intent(VerifyCodeActivity.this, NewPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    // Code is incorrect
                    Toast.makeText(VerifyCodeActivity.this, "Incorrect code. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
