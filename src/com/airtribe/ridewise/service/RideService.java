
package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.DistanceCalculator;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class RideService {

    private final RiderService riderService;
    private final DriverService driverService;
    private final FareStrategy fareStrategy;
    private final DistanceCalculator distanceCalculator;

    private final List<Ride> rides = new ArrayList<>();

    public RideService(
            RiderService riderService,
            DriverService driverService,
            FareStrategy fareStrategy,
            DistanceCalculator distanceCalculator) {

        this.riderService = riderService;
        this.driverService = driverService;
        this.fareStrategy = fareStrategy;
        this.distanceCalculator = distanceCalculator;
    }

    public Ride requestRide(String riderId, Location destination, RideMatchingStrategy rideMatchingStrategy) {

        if (destination == null) {
            throw new IllegalArgumentException("Destination cannot be null.");
        }

        if (rideMatchingStrategy == null) {
            throw new IllegalArgumentException("Ride-matching strategy cannot be null.");
        }

        Rider rider = riderService.getRiderById(riderId);

        if (rider == null) {
            throw new IllegalArgumentException("Rider not found: " + riderId);
        }

        double distance = distanceCalculator.calculateDistance(rider.getLocation(), destination);

        if (!Double.isFinite(distance) || distance <= 0) {
            throw new IllegalArgumentException(
                    "Calculated trip distance must be greater than zero.");
        }

        List<Driver> availableDrivers = driverService.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No available drivers.");
        }

        Driver driver = rideMatchingStrategy.findDriver(rider, availableDrivers);

        if (driver == null) {
            throw new NoDriverAvailableException("No suitable driver found.");
        }

        Ride ride = new Ride(
                IdGenerator.generateRideId(),
                rider,
                destination,
                distance
        );

        ride.assignDriver(driver);

        double fare = fareStrategy.calculateFare(ride);

        FareReceipt receipt = new FareReceipt(ride.getId(), fare);

        ride.attachFareReceipt(receipt);

        driverService.updateDriverAvailability(driver.getId(), false);

        driver.incrementActiveRideCount();

        rides.add(ride);

        return ride;
    }

    public Ride getRideById(String rideId) {
        for (Ride ride : rides) {
            if (ride.getId().equals(rideId)) {
                return ride;
            }
        }

        return null;
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides);
    }

    public void completeRide(String rideId) {
        Ride ride = getRideById(rideId);

        if (ride == null) {
            throw new IllegalArgumentException("Ride not found: " + rideId);
        }

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only assigned rides can be completed.");
        }

        Driver driver = ride.getDriver();

        ride.completeRide();

        driver.decrementActiveRideCount();

        driverService.updateDriverAvailability(driver.getId(), true);
    }

    public void cancelRide(String rideId) {
        Ride ride = getRideById(rideId);

        if (ride == null) {
            throw new IllegalArgumentException("Ride not found: " + rideId);
        }

        if (ride.getStatus() != RideStatus.REQUESTED && ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only requested or assigned rides can be cancelled.");
        }

        Driver driver = ride.getDriver();

        ride.cancelRide();

        if (driver != null) {
            driver.decrementActiveRideCount();

            driverService.updateDriverAvailability(driver.getId(), true);
        }
    }
}
