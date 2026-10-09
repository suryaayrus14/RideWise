package com.airtribe.ridewise.model;

public class Rider {

    private String id;
    private String name;

    private Location location;

    public Rider(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }
}
