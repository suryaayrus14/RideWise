package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Location;

public class HaversineDistanceCalculator implements DistanceCalculator{

    private static final double EARTH_RADIUS_KM = 6371;
    @Override
    public double calculateDistance(Location from, Location to){
        double lat1 = Math.toRadians(from.getLatitude());
        double lon1 = Math.toRadians(from.getLongitude());

        double lat2 = Math.toRadians(to.getLatitude());
        double lon2 = Math.toRadians(to.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // Distance in kilometers
    }


}
