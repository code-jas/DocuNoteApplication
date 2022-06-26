package com.angelo_silvestre.docunoteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class ForgotPassword extends AppCompatActivity {
    private final String Tag = "ForgotPassword";

    private Button vbtnRecovery,vbtnBackToLogin;
    private EditText vemailForgotPasword;
    private TextView vtxtMessageEmailErr;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViewsForgotPassword();

        vbtnBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
            startActivity(intent);

            finish();
        });

        vbtnRecovery.setOnClickListener(v -> {
            String email = vemailForgotPasword.getText().toString().trim();

            if(email.isEmpty()){
                vtxtMessageEmailErr.setText("Email is required");
            }
            else {
                // We have to send the password to recovery email
                vtxtMessageEmailErr.setText("");

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Mail Sent, You can recover your password  using your email", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                            } else {
                                Toast.makeText(this, "Email not registered or Email is wrong", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }

    private void initViewsForgotPassword() {

        vemailForgotPasword = findViewById(R.id.email_forgot_password);
        vbtnRecovery = findViewById(R.id.btn_recovery);
        vbtnBackToLogin = findViewById(R.id.btn_go_back_to_login);
        vtxtMessageEmailErr = findViewById(R.id.fp_email_err);

        firebaseAuth = FirebaseAuth.getInstance();

    }
}