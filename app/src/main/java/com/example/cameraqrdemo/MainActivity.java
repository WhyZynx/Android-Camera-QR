package com.example.cameraqrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View.OnClickListener openScanListener = v -> {
            Intent intent = new Intent(MainActivity.this, ScanQrActivity.class);
            startActivity(intent);
        };

        Button btnOpenScan = findViewById(R.id.btnOpenScan);
        View cardScanQr = findViewById(R.id.cardScanQr);

        if (btnOpenScan != null) {
            btnOpenScan.setOnClickListener(openScanListener);
        }
        if (cardScanQr != null) {
            cardScanQr.setOnClickListener(openScanListener);
        }

        Button btnGenerateQr = findViewById(R.id.btnGenerateQr);
        View cardGenerateQr = findViewById(R.id.cardGenerateQr);
        View.OnClickListener generateQrListener = v -> Toast.makeText(this, "Generate QR feature (Student 1)", Toast.LENGTH_SHORT).show();

        if (btnGenerateQr != null) {
            btnGenerateQr.setOnClickListener(generateQrListener);
        }
        if (cardGenerateQr != null) {
            cardGenerateQr.setOnClickListener(generateQrListener);
        }
    }
}