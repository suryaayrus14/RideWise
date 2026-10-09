package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.DistanceCalculator;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.HaversineDistanceCalculator;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;

import java.util.List;

public class RideWiseTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        runTest("Rider registration", RideWiseTest::testRiderRegistration);
        runTest("Driver and vehicle registration", RideWiseTest::testDriverRegistration);
        runTest("Distance calculation", RideWiseTest::testDistanceCalculation);
        runTest("Nearest driver selection", RideWiseTest::testNearestDriver);
        runTest("Ride request and fare", RideWiseTest::testRideRequest);
        runTest("Ride completion", RideWiseTest::testRideCompletion);
        runTest("Ride cancellation", RideWiseTest::testRideCancellation);
        runTest("No available driver", RideWiseTest::testNoAvailableDriver);

        System.out.println("\n========== TEST SUMMARY ==========");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("==================================");
    }

    private static void testRiderRegistration() {
        RiderService riderService = new RiderService();

        Rider rider = riderService.registerRider(
                "Test Rider",
                new Location(13.0827, 80.2707)
        );

        check(rider != null, "Rider should be created.");
        check(rider.getId() != null, "Rider should have an ID.");
        check(
                riderService.getRiderById(rider.getId()) != null,
                "Registered rider should be retrievable."
        );
    }

    private static void testDriverRegistration() {
        DriverService driverService = new DriverService();

        Driver driver = driverService.registerDriver(
                "Test Driver",
                new Location(13.0830, 80.2710),
                VehicleType.CAR
        );

        check(driver != null, "Driver should be created.");
        check(driver.getId() != null, "Driver should have an ID.");
        check(
                driver.getVehicleType() == VehicleType.CAR,
                "Driver should retain the registered vehicle type."
        );
        check(driver.isAvailable(), "New driver should be available.");
        check(
                driverService.getAvailableDrivers().contains(driver),
                "Driver should appear in the available-driver list."
        );
    }

    private static void testDistanceCalculation() {
        DistanceCalculator calculator = new HaversineDistanceCalculator();

        double distance = calculator.calculateDistance(
                new Location(13.0827, 80.2707),
                new Location(13.0927, 80.2707)
        );

        check(distance > 0, "Distance should be positive.");
        check(distance < 10, "Nearby locations should be less than 10 km apart.");
    }

    private static void testNearestDriver() {
        DriverService driverService = new DriverService();
        Rider rider = new Rider(
                "R-test",
                "Test Rider",
                new Location(13.0827, 80.2707)
        );

        Driver farDriver = driverService.registerDriver(
                "Far Driver",
                new Location(13.1500, 80.3500),
                VehicleType.CAR
        );

        Driver nearDriver = driverService.registerDriver(
                "Near Driver",
                new Location(13.0830, 80.2710),
                VehicleType.BIKE
        );

        DistanceCalculator calculator = new HaversineDistanceCalculator();

        Driver selected = new NearestDriverStrategy(calculator).findDriver(
                rider,
                List.of(farDriver, nearDriver)
        );

        check(
                selected == nearDriver,
                "Nearest driver should be selected."
        );
    }

    private static void testRideRequest() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        DistanceCalculator calculator = new HaversineDistanceCalculator();

        FareStrategy fareStrategy = new PeakHourFareStrategy(
                new DefaultFareStrategy()
        );

        RideService rideService = new RideService(
                riderService,
                driverService,
                fareStrategy,
                calculator
        );

        Rider rider = riderService.registerRider(
                "Test Rider",
                new Location(13.0827, 80.2707)
        );

        Driver driver = driverService.registerDriver(
                "Test Driver",
                new Location(13.0830, 80.2710),
                VehicleType.AUTO
        );

        Location destination = new Location(13.1000, 80.2900);

        Ride ride = rideService.requestRide(
                rider.getId(),
                destination,
                new NearestDriverStrategy(calculator)
        );

        check(ride.getDistance() > 0, "Ride distance should be calculated.");
        check(
                ride.getDestination() == destination,
                "Ride should retain the destination."
        );
        check(ride.getDriver() == driver, "Driver should be assigned.");
        check(
                ride.getDriver().getVehicleType() == VehicleType.AUTO,
                "Assigned driver's vehicle type should be accessible."
        );
        check(
                ride.getStatus() == RideStatus.ASSIGNED,
                "Ride status should be ASSIGNED."
        );
        check(
                ride.getFareReceipt() != null
                        && ride.getFareReceipt().getAmount() > 0,
                "A positive fare should be generated."
        );
        check(!driver.isAvailable(), "Assigned driver should be unavailable.");
    }

    private static void testRideCompletion() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        DistanceCalculator calculator = new HaversineDistanceCalculator();

        RideService rideService = new RideService(
                riderService,
                driverService,
                new DefaultFareStrategy(),
                calculator
        );

        Rider rider = riderService.registerRider(
                "Completion Rider",
                new Location(13.0827, 80.2707)
        );

        Driver driver = driverService.registerDriver(
                "Completion Driver",
                new Location(13.0830, 80.2710),
                VehicleType.BIKE
        );

        Ride ride = rideService.requestRide(
                rider.getId(),
                new Location(13.1000, 80.2900),
                new NearestDriverStrategy(calculator)
        );

        rideService.completeRide(ride.getId());

        check(
                ride.getStatus() == RideStatus.COMPLETED,
                "Ride should be COMPLETED."
        );
        check(driver.isAvailable(), "Driver should become available.");
        check(
                driver.getActiveRideCount() == 0,
                "Active ride count should return to zero."
        );
    }

    private static void testRideCancellation() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        DistanceCalculator calculator = new HaversineDistanceCalculator();

        RideService rideService = new RideService(
                riderService,
                driverService,
                new DefaultFareStrategy(),
                calculator
        );

        Rider rider = riderService.registerRider(
                "Cancellation Rider",
                new Location(13.0827, 80.2707)
        );

        Driver driver = driverService.registerDriver(
                "Cancellation Driver",
                new Location(13.0830, 80.2710),
                VehicleType.CAR
        );

        Ride ride = rideService.requestRide(
                rider.getId(),
                new Location(13.1000, 80.2900),
                new NearestDriverStrategy(calculator)
        );

        rideService.cancelRide(ride.getId());

        check(
                ride.getStatus() == RideStatus.CANCELLED,
                "Ride should be CANCELLED."
        );
        check(driver.isAvailable(), "Driver should become available.");
        check(
                driver.getActiveRideCount() == 0,
                "Active ride count should return to zero."
        );
    }

    private static void testNoAvailableDriver() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        DistanceCalculator calculator = new HaversineDistanceCalculator();

        RideService rideService = new RideService(
                riderService,
                driverService,
                new DefaultFareStrategy(),
                calculator
        );

        Rider rider = riderService.registerRider(
                "No Driver Rider",
                new Location(13.0827, 80.2707)
        );

        boolean exceptionThrown = false;

        try {
            rideService.requestRide(
                    rider.getId(),
                    new Location(13.1000, 80.2900),
                    new NearestDriverStrategy(calculator)
            );
        } catch (RuntimeException e) {
            exceptionThrown = true;
        }

        check(
                exceptionThrown,
                "Ride request should fail when no driver is available."
        );
    }

    private static void runTest(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable e) {
            failed++;
            System.out.println("[FAIL] " + name + ": " + e.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}