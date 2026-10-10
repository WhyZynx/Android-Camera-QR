# Camera QR Demo

## Table of Contents

- [Project Overview](#1-project-overview)
- [Technologies Used](#2-technologies-used)
- [Requirements](#3-requirements)
- [How to Run](#4-how-to-run)
- [How to Test](#5-how-to-test)
- [Common Issues](#6-common-issues)
- [Project Structure](#7-project-structure)
- [Notes and Limitations](#8-notes-and-limitations)

## 1. Project Overview

Camera QR Demo shows how to create and scan QR Codes for a sample sports court check-in on Android. The app creates a sample QR Code, asks for Camera permission when scanning, and checks the booking information and 5-minute time limit on the device. This is a local demo. It does not connect to a backend.

## 2. Technologies Used

| Technology | Purpose |
|---|---|
| Java | Activities and app logic |
| Android Studio, Gradle Kotlin DSL | Open, sync, and build the project |
| CameraX 1.4.1 | Show the camera preview and analyze camera images |
| Google ML Kit Barcode Scanning 17.3.0 | Read QR Codes from the camera |
| ZXing Core 3.5.3 | Create QR Code images |
| AndroidX AppCompat, Activity, ConstraintLayout, Material | App screens and Android support |

## 3. Requirements

- Android Studio that supports Android Gradle Plugin 9.3.3 and Gradle 9.5.0.
- Android SDK Platform 37 (`compileSdk` and `targetSdk` are 37). If Android Studio asks to install a missing SDK or Build Tools component, install it and wait for setup to finish.
- Minimum Android version: Android 7.0 (API 24).
- JDK 11 for Java source code. Android Studio usually includes a JDK. If needed, use the Gradle JDK selected in Android Studio.
- An Android phone or emulator with a working camera. A real phone is recommended for easier QR scanning.
- Internet access for the first Gradle Sync, so Gradle can download the required plugins and libraries.

## 4. How to Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/WhyZynx/Android-Camera-QR.git
   ```

2. **Open the project**: Start Android Studio, choose **Open**, and select the `Android-Camera-QR` folder you cloned.

3. **Sync Gradle**: Wait for Gradle Sync to finish. If Android Studio asks to install a missing Android SDK Platform or Build Tools component, install it and let Sync finish.

4. **Connect a device**: Connect your phone with a USB cable, enable **Developer options** and **USB debugging**, then accept the debugging prompt on your phone. Or start an emulator with a working camera and select its camera source in the emulator settings. A real phone is usually easier for QR scanning.

5. **Run the app**: Select the `app` run configuration, choose your device in the Android Studio toolbar, and click **Run** (▶).

6. **Allow Camera access**: The first time you open **Scan QR Code**, allow Camera access when Android asks. If you deny access, enable Camera permission in Android Settings before scanning.

## 5. How to Test

1. On the Home screen, choose **Generate Booking QR**. The app shows a sample QR Code with booking ID `BK001`, court `Badminton Court 01`, date `10/10/2026`, and time `18:00 - 19:00`. The QR Code is valid for 5 minutes.
2. To scan it with a phone, open **Scan QR Code** on the phone running the app and point the camera at the QR Code shown on another device. You can use two devices or show the QR Code on an external screen.
3. If the QR Code has all required fields and is not expired, the **Booking Information** screen shows the booking ID, court, date, and time. Choose **Confirm Check-in** to open **Check-in Successful**, then choose **Back Home**.
4. A QR Code without required fields or a valid timestamp is shown as invalid. A QR Code with all fields but older than 5 minutes is also rejected. **Scan Again** or **Cancel** returns to the scanner.

The app creates a QR Code with this format. The real `TIMESTAMP` value is the creation time in milliseconds:

```text
BOOKING_ID=BK001
COURT=Badminton Court 01
DATE=10/10/2026
TIME=18:00 - 19:00
TIMESTAMP=<creation time in milliseconds>
```

The four example lines in the prompt are not enough for validation. The scanner also needs `TIMESTAMP`, and the QR Code must still be within the time limit.

## 6. Common Issues

| Problem | Solution |
|---|---|
| Gradle Sync fails | Check your internet connection, Gradle JDK, and Android SDK Platform 37. Install any missing components and sync again. |
| Camera permission denied | Open Android Settings → Apps → CameraQRDemo → Permissions and allow Camera access. |
| Camera does not open | Check Camera permission and the device camera. For an emulator, check its camera source settings. |
| QR Code is not detected | Improve the lighting, hold the camera steady, keep the QR Code in the frame, and use a clear, unexpired code. |
| Android SDK Platform 37 is missing | Install Android SDK Platform 37 in SDK Manager or accept Android Studio's install prompt. |

## 7. Project Structure

```text
CameraQRDemo/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/cameraqrdemo/
│       │   ├── MainActivity.java
│       │   ├── BookingQrActivity.java
│       │   ├── ScanQrActivity.java
│       │   ├── ScanResultActivity.java
│       │   ├── CheckInSuccessActivity.java
│       │   └── Booking.java
│       └── res/layout/
├── gradle/libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew / gradlew.bat
```

- `MainActivity`: Home screen. Opens the QR creation or scanning screen.
- `BookingQrActivity`: Shows sample booking information and creates a QR Code that expires after 5 minutes.
- `ScanQrActivity`: Requests Camera permission, opens the back camera with CameraX, and reads QR Codes with ML Kit.
- `ScanResultActivity`: Reads the QR fields, checks their format and time limit, shows the result, and lets the user confirm check-in.
- `CheckInSuccessActivity`: Shows the demo success screen and a button to return Home.
- `Booking`: Holds booking information and converts it to QR Code text.

## 8. Notes and Limitations

- Booking information is created in the app. There is no screen to enter a booking.
- The app checks the `BOOKING_ID`, `COURT`, `DATE`, `TIME`, and `TIMESTAMP` fields and their time limit on the device. It does not verify a real booking with a backend, API, or database.
- The success screen is part of the demo. It does not save check-in information to a server.
- The scanner reads a QR Code and checks its data on the result screen. For testing, scan a current QR Code created by the app.
