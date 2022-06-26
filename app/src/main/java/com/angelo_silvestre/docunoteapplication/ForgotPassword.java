package com.angelo_silvestre.docunoteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPassword extends AppCompatActivity {
    private final String Tag = "ForgotPassword";

    Button vbtnRecovery,vbtnBackToLogin;
    EditText vemailForgotPasword;
    TextView vtxtMessageEmailErr;

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


            }
        });
    }

    private void initViewsForgotPassword() {

        vemailForgotPasword = findViewById(R.id.email_forgot_password);
        vbtnRecovery = findViewById(R.id.btn_recovery);
        vbtnBackToLogin = findViewById(R.id.btn_go_back_to_login);
        vtxtMessageEmailErr = findViewById(R.id.fp_email_err);

    }
}