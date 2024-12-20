package com.example.a19dhjetor2024;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;


public class LogInActivity extends AppCompatActivity {
    private EditText editEmailLog;
    private EditText editPasswordLog;
    private Button LogInBtnLog;

    private String generatedCode;
    private boolean isCodeValid = false;
    private Database DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editEmailLog = findViewById(R.id.editEmailLog);
        editPasswordLog = findViewById(R.id.editPasswordLog);
        LogInBtnLog = findViewById(R.id.LogInLogBtn);

        DB = new Database(this);

        LogInBtnLog.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        String email = editEmailLog.getText().toString().trim();
        String password = editPasswordLog.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
        } else {
            if (DB == null) {
                Toast.makeText(this, "Database not initialized!", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean validateUser = DB.validateUser(email, password);
            if (validateUser) {
                sendVerificationCode(email);
            } else {
                Toast.makeText(this, "Wrong Credentials!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendVerificationCode(String email) {
        generatedCode = generateRandomCode();
        isCodeValid = true;
        EmailSender.sendEmail(email, generatedCode);

        Toast.makeText(LogInActivity.this, "Verification code sent!", Toast.LENGTH_SHORT).show();



        Intent intent = new Intent(LogInActivity.this, VerifyActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", generatedCode);
        startActivity(intent);
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999)); // Generate a 6-digit code
    }
}
