package com.angelo_silvestre.docunoteapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StartUp extends AppCompatActivity {

    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);


        btnStart = findViewById(R.id.btn_startup);

        btnStart.setOnClickListener(view -> {

            Intent intent = new Intent(StartUp.this,MainActivity.class);
            startActivity(intent);

            finish();

        });
    }
}