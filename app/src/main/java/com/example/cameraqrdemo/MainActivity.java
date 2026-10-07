package com.example.cameraqrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGenerateQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGenerateQr = findViewById(R.id.btnGenerateQr);

        btnGenerateQr.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    BookingQrActivity.class
            );

            startActivity(intent);
        });
    }
}