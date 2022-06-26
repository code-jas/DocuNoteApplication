package com.angelo_silvestre.docunoteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private final String Tag = "Login";
    private EditText vloginEmail ,vloginPassword;
    private TextView vloginEmailErr , vloginPasswordErr;
    private Button vloginBtn;
    private TextView vgoToSignUp, vgoToForgotPassword;
    private TextInputLayout vloginEmailLayout, vloginPasswordLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
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

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null;
                                if(firebaseUser.isEmailVerified()){
                                    Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                }
                            } else {
                                Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show();
                            }
                        });

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
            finish();
        }
    }
}