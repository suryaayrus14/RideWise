package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {

        Driver leastActiveDriver = null;
        int minActiveRides = Integer.MAX_VALUE;

        for (Driver driver : availableDrivers) {

            if (driver.getActiveRideCount() < minActiveRides) {
                minActiveRides = driver.getActiveRideCount();
                leastActiveDriver = driver;
            }
        }

        return leastActiveDriver;
    }
}
