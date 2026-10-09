package com.example.cameraqrdemo;

public class Booking {

    private final String bookingId;
    private final String court;
    private final String date;
    private final String time;

    public Booking(String bookingId, String court, String date, String time) {
        this.bookingId = bookingId;
        this.court = court;
        this.date = date;
        this.time = time;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCourt() {
        return court;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String toQrData() {
        return "BOOKING_ID=" + bookingId + "\n"
                + "COURT=" + court + "\n"
                + "DATE=" + date + "\n"
                + "TIME=" + time;
    }
}