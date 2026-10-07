package com.example.cameraqrdemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BookingQrActivity extends AppCompatActivity {

    private TextView txtBookingId;
    private TextView txtCourt;
    private TextView txtDate;
    private TextView txtTime;

    private ImageView imgQrCode;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_qr);

        initViews();

        Booking booking = createBooking();

        displayBookingInfo(booking);
        generateBookingQr(booking);

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        txtBookingId = findViewById(R.id.txtBookingId);
        txtCourt = findViewById(R.id.txtCourt);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);

        imgQrCode = findViewById(R.id.imgQrCode);
        btnBack = findViewById(R.id.btnBack);
    }

    private Booking createBooking() {
        return new Booking(
                "BK001",
                "Badminton Court 01",
                "10/10/2026",
                "18:00 - 19:00"
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

    private Bitmap createQrBitmap(String data) throws WriterException {

        int size = 700;

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
}