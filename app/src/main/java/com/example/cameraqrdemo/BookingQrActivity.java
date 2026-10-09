package com.example.cameraqrdemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Locale;

public class BookingQrActivity extends AppCompatActivity {

    private static final long EXPIRE_DURATION_MS = 5 * 60 * 1000L;

    private TextView txtBookingId;
    private TextView txtCourt;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtTimer;

    private ImageView imgQrCode;
    private ImageButton btnBack;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_qr);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        Booking booking = createBooking();

        displayBookingInfo(booking);
        generateBookingQr(booking);
        startExpirationTimer();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        txtBookingId = findViewById(R.id.txtBookingId);
        txtCourt = findViewById(R.id.txtCourt);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtTimer = findViewById(R.id.txtTimer);

        imgQrCode = findViewById(R.id.imgQrCode);
        btnBack = findViewById(R.id.btnBack);
    }

    private Booking createBooking() {
        return new Booking(
                "BK001",
                "Badminton Court 01",
                "10/10/2026",
                "18:00 - 19:00",
                System.currentTimeMillis()
        );
    }

    private void displayBookingInfo(Booking booking) {
        txtBookingId.setText(booking.getBookingId());
        txtCourt.setText(booking.getCourt());
        txtDate.setText(booking.getDate());
        txtTime.setText(booking.getTime());
    }

    private void generateBookingQr(Booking booking) {
        try {
            Bitmap qrBitmap = createQrBitmap(booking.toQrData());
            imgQrCode.setImageBitmap(qrBitmap);

        } catch (WriterException e) {
            Toast.makeText(
                    this,
                    R.string.qr_generation_error,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void startExpirationTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(EXPIRE_DURATION_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                txtTimer.setText(getString(R.string.scan_for_checkin_timer, timeFormatted));
            }

            @Override
            public void onFinish() {
                txtTimer.setText(getString(R.string.qr_expired_notice));
            }
        }.start();
    }

    private Bitmap createQrBitmap(String data) throws WriterException {

        int size = 800;

        BitMatrix matrix = new MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE,
                size,
                size
        );

        Bitmap bitmap = Bitmap.createBitmap(
                size,
                size,
                Bitmap.Config.RGB_565
        );

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                int color = matrix.get(x, y)
                        ? Color.BLACK
                        : Color.WHITE;

                bitmap.setPixel(x, y, color);
            }
        }

        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
