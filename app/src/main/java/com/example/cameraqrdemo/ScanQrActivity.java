package com.example.cameraqrdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;

public class ScanQrActivity extends AppCompatActivity {

    private static final String TAG = "ScanQrActivity";

    private PreviewView previewView;
    private Button btnCancel;

    // Biến cờ tránh mở ScanResultActivity nhiều lần liên tục khi đã quét được QR
    private boolean isQrDetected = false;

    // ML Kit Barcode Scanner
    private BarcodeScanner barcodeScanner;

    // Launcher yêu cầu quyền Camera
    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Cần cấp quyền Camera để quét mã QR", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_qr);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainScanQr), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View
        previewView = findViewById(R.id.previewView);
        btnCancel = findViewById(R.id.btnCancel);

        // Khởi tạo ML Kit Barcode Scanner chỉ nhận dạng định dạng QR Code
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        barcodeScanner = BarcodeScanning.getClient(options);

        // Xử lý nút Cancel
        btnCancel.setOnClickListener(v -> finish());

        // Kiểm tra quyền và mở Camera
        checkCameraPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset cờ nhận diện khi quay lại màn hình này từ ScanResultActivity
        isQrDetected = false;
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // 1. Cấu hình Camera Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // 2. Cấu hình ImageAnalysis để xử lý từng frame ảnh từ Camera
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    // Nếu đã phát hiện QR trước đó, bỏ qua frame hiện tại
                    if (isQrDetected) {
                        imageProxy.close();
                        return;
                    }

                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.getImageInfo().getRotationDegrees()
                        );

                        // Dùng ML Kit phân tích hình ảnh
                        barcodeScanner.process(image)
                                .addOnSuccessListener(barcodes -> {
                                    if (isQrDetected) {
                                        return;
                                    }
                                    for (Barcode barcode : barcodes) {
                                        String rawValue = barcode.getRawValue();
                                        if (rawValue != null && !rawValue.isEmpty()) {
                                            isQrDetected = true;

                                            // Mở ScanResultActivity và truyền rawValue qua Intent
                                            Intent intent = new Intent(ScanQrActivity.this, ScanResultActivity.class);
                                            intent.putExtra("QR_DATA", rawValue);
                                            startActivity(intent);
                                            break;
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Phân tích QR thất bại", e))
                                .addOnCompleteListener(task -> imageProxy.close());
                    } else {
                        imageProxy.close();
                    }
                });

                // Chọn camera sau
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Unbind tất cả use case cũ trước khi bind
                cameraProvider.unbindAll();

                // Bind Preview và ImageAnalysis vào lifecycle của Activity
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Lỗi khi khởi chạy Camera", e);
                Toast.makeText(this, "Không thể mở Camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (barcodeScanner != null) {
            barcodeScanner.close();
        }
    }
}