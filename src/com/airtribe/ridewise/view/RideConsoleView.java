
package com.airtribe.ridewise.view;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;

import java.util.List;

public class RideConsoleView {

    public void displayAvailableDrivers(List<Driver> drivers) {
        System.out.println("\n===== Available Drivers =====");

        if (drivers.isEmpty()) {
            System.out.println("No drivers are currently available.");
            return;
        }

        for (Driver driver : drivers) {
            System.out.printf(
                    "Driver ID: %s | Name: %s | Vehicle: %s | Active rides: %d%n",
                    driver.getId(),
                    driver.getName(),
                    driver.getVehicleType(),
                    driver.getActiveRideCount()
            );
        }
    }

    public void displayAllRides(List<Ride> rides) {
        System.out.println("\n===== All Rides =====");

        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }

        for (Ride ride : rides) {
            String driverName = ride.getDriver() == null
                    ? "Not assigned"
                    : ride.getDriver().getName();

            String vehicleType = ride.getDriver() == null
                    ? "N/A"
                    : ride.getDriver().getVehicleType().toString();

            String fare = ride.getFareReceipt() == null
                    ? "N/A"
                    : String.format(
                    "₹%.2f",
                    ride.getFareReceipt().getAmount()
            );

            System.out.printf(
                    "Ride ID: %s | Rider: %s | Driver: %s | Vehicle: %s"
                            + " | Destination: (%.4f, %.4f)"
                            + " | Distance: %.2f km | Status: %s | Fare: %s%n",
                    ride.getId(),
                    ride.getRider().getName(),
                    driverName,
                    vehicleType,
                    ride.getDestination().getLatitude(),
                    ride.getDestination().getLongitude(),
                    ride.getDistance(),
                    ride.getStatus(),
                    fare
            );
        }
    }
}
