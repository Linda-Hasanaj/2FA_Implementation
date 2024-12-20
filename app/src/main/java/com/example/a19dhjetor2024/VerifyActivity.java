package com.example.a19dhjetor2024;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class VerifyActivity extends AppCompatActivity{
    private EditText veditOTPCode;
    private Button VerifyBtn;
    private Button ResendBtn;
    private String receivedCode;
    private String email;
    private boolean isCodeValid = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify);

        veditOTPCode = findViewById(R.id.editOTPCode);
        VerifyBtn = findViewById(R.id.VerifyBtn);
        ResendBtn = findViewById(R.id.ResendBtn);

        email = getIntent().getStringExtra("email");
        receivedCode = getIntent().getStringExtra("code");
        if (email == null || receivedCode == null) {
            Log.e("VerificationActivity", "Email or code is null!");
            return;
        }

        // Kontrolloni për rootView pa përdorur else
        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        VerifyBtn.setOnClickListener(view -> verify());
        ResendBtn.setOnClickListener(view -> resendCode());
    }

    private void verify() {
        String enteredCode = veditOTPCode.getText().toString();

        if (enteredCode.equals(receivedCode) && isCodeValid) {
            Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show();
        } else {
            VerifyBtn.setEnabled(false);
            Toast.makeText(this, "Verification code expired or code not correct!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendCode() {
        String newCode = generateRandomCode();
        EmailSender.sendEmail(email, newCode);
        receivedCode = newCode;

        Toast.makeText(this, "Verification code resent!", Toast.LENGTH_SHORT).show();
        VerifyBtn.setEnabled(true);
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
