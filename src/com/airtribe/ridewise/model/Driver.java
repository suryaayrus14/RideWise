package com.airtribe.ridewise.model;

public class Driver {
    private String id;
    private String name;
    private Location currentLocation;
    private boolean available;

    private final VehicleType vehicleType;

    private int activeRideCount;

    public Driver(String id, String name, Location currentLocation, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicleType = vehicleType;
        this.available = true; // By default, a new driver is available
        this.activeRideCount = 0;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Location getCurrentLocation() {
        return currentLocation;
    }
    public boolean isAvailable() {
        return available;
    }
    public VehicleType getVehicleType() {return vehicleType;}

    public void markAvailable() {
        this.available = true;
    }
    public void markUnavailable() {
        this.available = false;
    }

    public int getActiveRideCount() {return activeRideCount;}

    public void incrementActiveRideCount() {activeRideCount++;}

    public void decrementActiveRideCount() {
        if (activeRideCount > 0) {
            activeRideCount--;
        }
    }
}
