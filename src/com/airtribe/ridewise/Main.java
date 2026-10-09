package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.DistanceCalculator;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.HaversineDistanceCalculator;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.view.RideConsoleView;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final RiderService riderService = new RiderService();
    private static final DriverService driverService = new DriverService();

    private static final DistanceCalculator distanceCalculator =
            new HaversineDistanceCalculator();

    private static final FareStrategy fareStrategy =
            new PeakHourFareStrategy(new DefaultFareStrategy());

    private static final RideService rideService =
            new RideService(
                    riderService,
                    driverService,
                    fareStrategy,
                    distanceCalculator
            );

    private static final RideConsoleView consoleView = new RideConsoleView();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            displayMenu();

            try {
                int choice = readInt("Enter your choice: ");

                switch (choice) {
                    case 1 -> registerRider();
                    case 2 -> registerDriver();
                    case 3 -> consoleView.displayAvailableDrivers(
                            driverService.getAvailableDrivers()
                    );
                    case 4 -> requestRide();
                    case 5 -> completeRide();
                    case 6 -> cancelRide();
                    case 7 -> consoleView.displayAllRides(
                            rideService.getAllRides()
                    );
                    case 8 -> {
                        running = false;
                        System.out.println("Exiting RideWise. Goodbye!");
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }

            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n========== RIDEWISE ==========");
        System.out.println("1. Register Rider");
        System.out.println("2. Register Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. Cancel Ride");
        System.out.println("7. View All Rides");
        System.out.println("8. Exit");
        System.out.println("==============================");
    }

    private static void registerRider() {
        String name = readText("Enter rider name: ");
        Location pickup = readLocation("pickup");

        Rider rider = riderService.registerRider(name, pickup);

        System.out.println("Rider registered successfully.");
        System.out.println("Rider ID: " + rider.getId());
    }

    private static void registerDriver() {
        String name = readText("Enter driver name: ");
        Location location = readLocation("driver");

        VehicleType vehicleType = chooseVehicleType();

        Driver driver = driverService.registerDriver(
                name,
                location,
                vehicleType
        );

        System.out.println("Driver registered successfully.");
        System.out.println("Driver ID: " + driver.getId());
        System.out.println("Vehicle type: " + driver.getVehicleType());
    }

    private static VehicleType chooseVehicleType() {
        System.out.println("\nChoose vehicle type:");
        System.out.println("1. Bike");
        System.out.println("2. Auto");
        System.out.println("3. Car");

        int choice = readInt("Enter your choice: ");

        return switch (choice) {
            case 1 -> VehicleType.BIKE;
            case 2 -> VehicleType.AUTO;
            case 3 -> VehicleType.CAR;
            default -> throw new IllegalArgumentException(
                    "Invalid vehicle type."
            );
        };
    }

    private static void requestRide() {
        String riderId = readText("Enter rider ID: ");
        Location destination = readLocation("destination");

        RideMatchingStrategy matchingStrategy = chooseMatchingStrategy();

        Ride ride = rideService.requestRide(
                riderId,
                destination,
                matchingStrategy
        );

        System.out.println("\nRide requested successfully!");
        System.out.println("Ride ID: " + ride.getId());
        System.out.println("Driver: " + ride.getDriver().getName());
        System.out.println(
                "Vehicle type: " + ride.getDriver().getVehicleType()
        );

        System.out.printf(
                "Calculated distance: %.2f km%n",
                ride.getDistance()
        );

        if (ride.getFareReceipt() != null) {
            System.out.printf(
                    "Estimated fare: ₹%.2f%n",
                    ride.getFareReceipt().getAmount()
            );
        }

        System.out.println("Status: " + ride.getStatus());
    }

    private static RideMatchingStrategy chooseMatchingStrategy() {
        System.out.println("\nChoose driver matching strategy:");
        System.out.println("1. Nearest Driver");
        System.out.println("2. Least Active Driver");

        int choice = readInt("Enter your choice: ");

        return switch (choice) {
            case 1 -> new NearestDriverStrategy(distanceCalculator);
            case 2 -> new LeastActiveDriverStrategy();
            default -> throw new IllegalArgumentException(
                    "Invalid driver matching strategy."
            );
        };
    }

    private static void completeRide() {
        String rideId = readText("Enter ride ID to complete: ");
        rideService.completeRide(rideId);
        System.out.println("Ride completed successfully.");
    }

    private static void cancelRide() {
        String rideId = readText("Enter ride ID to cancel: ");
        rideService.cancelRide(rideId);
        System.out.println("Ride cancelled successfully.");
    }

    private static Location readLocation(String type) {
        System.out.println("Enter " + type + " location coordinates:");

        double latitude = readDouble("Latitude (-90 to 90): ");
        double longitude = readDouble("Longitude (-180 to 180): ");

        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90."
            );
        }

        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180."
            );
        }

        return new Location(latitude, longitude);
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        return value;
    }

    private static int readInt(String prompt) {
        try {
            return Integer.parseInt(readText(prompt));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid integer."
            );
        }
    }

    private static double readDouble(String prompt) {
        try {
            double value = Double.parseDouble(readText(prompt));

            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException(
                        "Please enter a finite number."
                );
            }

            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid number."
            );
        }
    }
}