package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class DriverService {

    private final List<Driver> drivers = new ArrayList<>();

    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        String id = IdGenerator.generateDriverId();
        Driver driver = new Driver(id, name, location, vehicleType);
        drivers.add(driver);
        return driver;
    }

    public Driver getDriverById(String id) {
        for (Driver driver : drivers) {
            if (driver.getId().equals(id)) {
                return driver;
            }
        }
        return null;
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    public void updateDriverAvailability(String id, boolean available) {
        Driver driver = getDriverById(id);

        if (driver != null) {
            if (available) {
                driver.markAvailable();
            } else {
                driver.markUnavailable();
            }
        }
    }
}
