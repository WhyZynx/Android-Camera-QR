package com.example.cameraqrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScanResultActivity extends AppCompatActivity {

    private TextView tvRawContent;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainScanResult), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View
        tvRawContent = findViewById(R.id.tvRawContent);
        btnBack = findViewById(R.id.btnBack);

        // Lấy dữ liệu QR từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("QR_DATA")) {
            String qrData = intent.getStringExtra("QR_DATA");
            tvRawContent.setText(qrData);
        }

        // Nút Back
        btnBack.setOnClickListener(v -> finish());
    }
}