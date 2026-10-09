package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy{

    private static final double BASE_FARE = 50.0;
    private static final double COST_PER_KM = 10.0;

    @Override
    public double calculateFare(Ride ride) {
        double distance = ride.getDistance();
        return BASE_FARE + (COST_PER_KM * distance);
    }
}
