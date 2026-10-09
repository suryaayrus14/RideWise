
package com.airtribe.ridewise.model;

import java.time.LocalDateTime;

public class Ride {

    private final String id;
    private final Rider rider;
    private final Location destination;
    private final double distance;
    private final LocalDateTime requestedAt;

    private Driver driver;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(String id, Rider rider, Location destination, double distance) {

        if (distance <= 0 || !Double.isFinite(distance)) {
            throw new IllegalArgumentException("Distance must be a finite number greater than zero.");
        }

        this.id = id;
        this.rider = rider;
        this.destination = destination;
        this.distance = distance;
        this.requestedAt = LocalDateTime.now();
        this.status = RideStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Location getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public Driver getDriver() {
        return driver;
    }

    public RideStatus getStatus() {
        return status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void assignDriver(Driver driver) {
        if (status != RideStatus.REQUESTED) {
            throw new IllegalStateException("Only requested rides can be assigned.");
        }

        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null.");
        }
        this.driver = driver;
        this.status = RideStatus.ASSIGNED;
    }

    public void completeRide() {
        if (status != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only assigned rides can be completed.");
        }
        this.status = RideStatus.COMPLETED;
    }

    public void cancelRide() {
        if (status != RideStatus.REQUESTED && status != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only requested or assigned rides can be cancelled.");
        }
        this.status = RideStatus.CANCELLED;
    }

    public void attachFareReceipt(FareReceipt fareReceipt) {
        if (fareReceipt == null) {
            throw new IllegalArgumentException("Fare receipt cannot be null.");
        }
        this.fareReceipt = fareReceipt;
    }
}
