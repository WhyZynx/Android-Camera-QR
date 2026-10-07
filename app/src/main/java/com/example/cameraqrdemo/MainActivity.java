package com.example.cameraqrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mở màn hình quét QR (Student 2)
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

        // Chức năng Tạo QR (Student 1)
        Button btnGenerateQr = findViewById(R.id.btnGenerateQr);
        View cardGenerateQr = findViewById(R.id.cardGenerateQr);
        View.OnClickListener generateQrListener = v -> {
            Toast.makeText(this, "Chức năng Generate QR của Student 1", Toast.LENGTH_SHORT).show();
        };

        if (btnGenerateQr != null) {
            btnGenerateQr.setOnClickListener(generateQrListener);
        }
        if (cardGenerateQr != null) {
            cardGenerateQr.setOnClickListener(generateQrListener);
        }
    }
}