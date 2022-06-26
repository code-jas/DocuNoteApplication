package com.angelo_silvestre.docunoteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private final String Tag = "Login";
    private EditText vloginEmail ,vloginPassword;
    private TextView vloginEmailErr , vloginPasswordErr;
    private Button vloginBtn;
    private TextView vgoToSignUp, vgoToForgotPassword;
    private TextInputLayout vloginEmailLayout, vloginPasswordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewsLogin();

        vgoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
            finish();
        });
        vgoToForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
            startActivity(intent);
            finish();
        });

        vloginBtn.setOnClickListener(v -> {
            String email = vloginEmail.getText().toString().trim();
            String password = vloginPassword.getText().toString().trim();
            if(email.isEmpty()){
                vloginEmailErr.setText("Email is required");
            }
            if(password.isEmpty()){
                vloginPasswordErr.setText("Password is required");
            }
            else {
                // Login the user
                vloginEmailErr.setText("");
                vloginPasswordErr.setText("");

            }
        });








    }


    private void initViewsLogin() {
        vloginEmail = findViewById(R.id.login_email_editText);
        vloginPassword = findViewById(R.id.login_password_editText);
        vloginEmailErr = findViewById(R.id.login_email_err);
        vloginPasswordErr = findViewById(R.id.login_password_err);
        vloginBtn = findViewById(R.id.login_btn);
        vgoToSignUp = findViewById(R.id.goto_create_account);
        vgoToForgotPassword = findViewById(R.id.goto_forgot_password);
        vloginEmailLayout = findViewById(R.id.si_email_textInputLayout);
        vloginPasswordLayout = findViewById(R.id.si_password_textInputLayout);


    }
}