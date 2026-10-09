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

public class CheckInSuccessActivity extends AppCompatActivity {

    private TextView tvBookingId;
    private TextView tvCourt;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_in_success);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainCheckInSuccess), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int p24 = (int) (24 * getResources().getDisplayMetrics().density);
            v.setPadding(
                    systemBars.left + p24,
                    systemBars.top + p24,
                    systemBars.right + p24,
                    systemBars.bottom + p24
            );
            return insets;
        });

        tvBookingId = findViewById(R.id.tvBookingId);
        tvCourt = findViewById(R.id.tvCourt);
        btnBackHome = findViewById(R.id.btnBackHome);

        Intent intent = getIntent();
        String bookingId = "N/A";
        String court = "N/A";

        if (intent != null) {
            if (intent.hasExtra("BOOKING_ID")) {
                bookingId = intent.getStringExtra("BOOKING_ID");
            }
            if (intent.hasExtra("COURT")) {
                court = intent.getStringExtra("COURT");
            }
        }

        tvBookingId.setText(bookingId);
        tvCourt.setText(court);

        btnBackHome.setOnClickListener(v -> {
            Intent intentHome = new Intent(CheckInSuccessActivity.this, MainActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentHome);
            finish();
        });
    }
}
