package com.example.AtCapacity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Document(collection = "facilities")
public class Facility {

    @Id
    private String id;
    private String name;
    private int currentOccupancy;
    private int totalCapacity;
    private Location location;
    private String owner;   // Field for owner (e.g., company/organization name)
    private String hours;   // Operating hours in "HH:mm-HH:mm" format (e.g., "06:30-22:00")
    private String link;    // New field for a website or resource link
    private FacilityType type; // New field for facility type
    private boolean isOpen;
    private String address;

    // Constructors
    public Facility() {
    }

    public Facility(String name, int totalCapacity, Location location, String owner, String hours, String link, FacilityType type) {
        this.name = name;
        this.currentOccupancy = 0;
        this.totalCapacity = totalCapacity;
        this.location = location;
        this.owner = owner;
        this.hours = hours;
        this.link = link;
        this.type = type;
        this.isOpen();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    // Business methods
    public boolean incrementOccupancy() {
        return currentOccupancy < totalCapacity && ++currentOccupancy > 0;
    }

    public boolean decrementOccupancy() {
        return currentOccupancy > 0 && --currentOccupancy >= 0;
    }

    public boolean isFull() {
        return currentOccupancy >= totalCapacity;
    }

    public double getOccupancyPercentage() {
        return ((double) currentOccupancy / totalCapacity) * 100;
    }

    // Method to check if the facility is open
    public void isOpen() {
        if (hours == null || hours.isEmpty()) {
            isOpen = false;
            return; // No hours specified
        }

        // Split the hours into opening and closing times
        String[] times = hours.split("-");
        if (times.length != 2) {
            isOpen = false; // Invalid format
        }

        LocalTime openingTime = LocalTime.parse(times[0]); // Parse opening time
        LocalTime closingTime = LocalTime.parse(times[1]); // Parse closing time
        LocalTime now = LocalTime.now(); // Get the current time

        // Check if the current time is within the range
        isOpen = !now.isBefore(openingTime) && !now.isAfter(closingTime);
    }
}