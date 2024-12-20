package com.example.a19dhjetor2024;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText editName, editSurname, editEmail, editPassword, editConfirmPassword;
    private Database db;
    private Button SignUpBtn;
    private Button LogInBtn;
    private Button verifyBtn;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        LogInBtn = findViewById(R.id.LogInBtn);
        SignUpBtn = findViewById(R.id.SignUpBtn);

        db = new Database(this);

        SignUpBtn.setOnClickListener(v -> {
            if (validateFields()) {
                String name = editName.getText().toString().trim();
                String surname = editSurname.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (db.checkAdminEmail(email)) {
                    Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show();
                } else if (db.insertAdminUser(name, surname, email, password)) {
                    String verificationCode = generateRandomCode();
                    EmailSender.sendEmail(email, verificationCode);

                    Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", verificationCode);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LogInBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });

        verifyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);
            startActivity(intent);
        });
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private boolean validateFields() {
        String name = editName.getText().toString().trim();
        String surname = editSurname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || !name.matches("[a-zA-Z]+")) {
            editName.setError("Name must contain only letters and not be empty.");
            return false;
        }

        if (TextUtils.isEmpty(surname) || !surname.matches("[a-zA-Z]+")) {
            editSurname.setError("Surname must contain only letters and not be empty.");
            return false;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email address.");
            return false;
        }

        if (TextUtils.isEmpty(password) ||
                !Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{6,20}$").matcher(password).matches()) {
            editPassword.setError("Password must contain 1 lowercase, 1 uppercase, 1 digit, 1 special character, and be 6-20 characters long.");
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfirmPassword.setError("Please confirm your password.");
            return false;
        } else if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match.");
            return false;
        }

        return true;
    }
}
