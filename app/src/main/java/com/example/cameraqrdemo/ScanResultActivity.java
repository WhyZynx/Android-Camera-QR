package com.example.cameraqrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScanResultActivity extends AppCompatActivity {

    private LinearLayout layoutValid;
    private LinearLayout layoutInvalid;

    private TextView tvBookingId;
    private TextView tvCourt;
    private TextView tvDate;
    private TextView tvTime;

    private Button btnConfirmCheckIn;
    private Button btnCancel;
    private Button btnScanAgain;

    private String bookingId;
    private String court;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainScanResult), (v, insets) -> {
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

        layoutValid = findViewById(R.id.layoutValid);
        layoutInvalid = findViewById(R.id.layoutInvalid);

        tvBookingId = findViewById(R.id.tvBookingId);
        tvCourt = findViewById(R.id.tvCourt);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        btnConfirmCheckIn = findViewById(R.id.btnConfirmCheckIn);
        btnCancel = findViewById(R.id.btnCancel);
        btnScanAgain = findViewById(R.id.btnScanAgain);

        Intent intent = getIntent();
        String qrData = (intent != null) ? intent.getStringExtra("QR_DATA") : null;

        parseAndValidateQrData(qrData);

        btnConfirmCheckIn.setOnClickListener(v -> {
            Intent intentSuccess = new Intent(ScanResultActivity.this, CheckInSuccessActivity.class);
            intentSuccess.putExtra("BOOKING_ID", bookingId);
            intentSuccess.putExtra("COURT", court);
            startActivity(intentSuccess);
        });

        btnCancel.setOnClickListener(v -> finish());

        btnScanAgain.setOnClickListener(v -> finish());
    }

    private void parseAndValidateQrData(String qrData) {
        bookingId = null;
        court = null;
        String date = null;
        String time = null;

        if (qrData != null && !qrData.trim().isEmpty()) {
            String[] lines = qrData.split("\\r?\\n");
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("BOOKING_ID=")) {
                    bookingId = line.substring("BOOKING_ID=".length()).trim();
                } else if (line.startsWith("COURT=")) {
                    court = line.substring("COURT=".length()).trim();
                } else if (line.startsWith("DATE=")) {
                    date = line.substring("DATE=".length()).trim();
                } else if (line.startsWith("TIME=")) {
                    time = line.substring("TIME=".length()).trim();
                }
            }
        }

        boolean isValid = bookingId != null && !bookingId.isEmpty()
                && court != null && !court.isEmpty()
                && date != null && !date.isEmpty()
                && time != null && !time.isEmpty();

        if (isValid) {
            layoutValid.setVisibility(View.VISIBLE);
            layoutInvalid.setVisibility(View.GONE);

            tvBookingId.setText(bookingId);
            tvCourt.setText(court);
            tvDate.setText(date);
            tvTime.setText(time);
        } else {
            layoutValid.setVisibility(View.GONE);
            layoutInvalid.setVisibility(View.VISIBLE);
        }
    }
}
