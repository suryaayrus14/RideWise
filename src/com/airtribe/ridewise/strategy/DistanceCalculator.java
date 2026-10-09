package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Location;

public interface DistanceCalculator {

    double calculateDistance(Location from, Location to);
}
