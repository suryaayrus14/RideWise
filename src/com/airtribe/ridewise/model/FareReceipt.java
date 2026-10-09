package com.airtribe.ridewise.model;

import java.time.LocalDateTime;

public class FareReceipt {

    private String rideId;
    private double amount;
    private LocalDateTime generatedAt;

    public FareReceipt(String rideId, double amount) {
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = LocalDateTime.now();
    }

    public String getRideId() {
        return rideId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}