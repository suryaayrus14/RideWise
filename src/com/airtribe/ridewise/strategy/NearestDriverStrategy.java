package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy{
        private final DistanceCalculator distanceCalculator;

        public NearestDriverStrategy (DistanceCalculator distanceCalculator) {
            this.distanceCalculator = distanceCalculator;
        }

        @Override
        public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
            Driver nearestDriver = null;
            double minDistance = Double.MAX_VALUE;

            for (Driver driver : availableDrivers) {
                double distance = distanceCalculator.calculateDistance(rider.getLocation(), driver.getCurrentLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestDriver = driver;
                }
            }

            return nearestDriver;
        }


}
