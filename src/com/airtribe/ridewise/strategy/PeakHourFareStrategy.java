
package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {

    private static final double PEAK_HOUR_MULTIPLIER = 1.5;

    private final FareStrategy defaultFareStrategy;

    public PeakHourFareStrategy(FareStrategy defaultFareStrategy) {
        this.defaultFareStrategy = defaultFareStrategy;
    }

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = defaultFareStrategy.calculateFare(ride);

        LocalTime requestTime = ride.getRequestedAt().toLocalTime();

        boolean morningPeak = !requestTime.isBefore(LocalTime.of(8, 0))
                        && requestTime.isBefore(LocalTime.of(10, 0));

        boolean eveningPeak = !requestTime.isBefore(LocalTime.of(17, 0))
                        && requestTime.isBefore(LocalTime.of(20, 0));

        if (morningPeak || eveningPeak) {
            return baseFare * PEAK_HOUR_MULTIPLIER;
        }

        return baseFare;
    }
}
