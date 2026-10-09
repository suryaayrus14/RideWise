package com.airtribe.ridewise.util;

public class IdGenerator {

    private static int riderCounter = 0;
    private static int driverCounter = 0;
    private static int rideCounter = 0;

    public static String generateRiderId() {
        return String.format("R%03d", ++riderCounter);
    }

    public static String generateDriverId() {
        return String.format("D%03d", ++driverCounter);
    }

    public static String generateRideId() {
        return String.format("RIDE%03d", ++rideCounter);
    }
}