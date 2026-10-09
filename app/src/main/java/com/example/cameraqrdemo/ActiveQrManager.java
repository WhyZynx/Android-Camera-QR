package com.example.cameraqrdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class ActiveQrManager {

    private static final String PREF_NAME = "active_qr_prefs";
    private static final String KEY_TOKEN = "active_qr_token";
    private static final String KEY_TIMESTAMP = "active_qr_timestamp";
    private static final long EXPIRE_DURATION_MS = 5 * 60 * 1000L;

    public static synchronized void setActiveQr(Context context, String token, long timestamp) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putLong(KEY_TIMESTAMP, timestamp)
                .apply();
    }

    public static synchronized void invalidateActiveQr(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static synchronized boolean isQrActiveAndValid(Context context, String token, long timestamp) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String activeToken = prefs.getString(KEY_TOKEN, null);

        if (activeToken == null || !activeToken.equals(token)) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - timestamp;
        return elapsed >= 0 && elapsed <= EXPIRE_DURATION_MS;
    }

    public static synchronized boolean isSessionClosed(Context context, String token) {
        if (token == null || token.trim().isEmpty()) {
            return true;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String activeToken = prefs.getString(KEY_TOKEN, null);
        return activeToken == null || !activeToken.equals(token);
    }
}
