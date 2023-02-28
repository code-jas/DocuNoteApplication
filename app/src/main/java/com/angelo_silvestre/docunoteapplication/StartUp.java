package com.angelo_silvestre.docunoteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class StartUp extends AppCompatActivity {

    Button btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        Objects.requireNonNull(getSupportActionBar()).hide();
        btnStart = findViewById(R.id.btn_startup);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // setting the user direct to the notes when  the user is already logged in
        if(firebaseUser != null){
            Intent intent = new Intent(StartUp.this, NotesActivity.class);
            startActivity(intent);
            finish();
        }


        btnStart.setOnClickListener(view -> {

            Intent intent = new Intent(StartUp.this,MainActivity.class);
            startActivity(intent);

            finish();

        });
    }
}