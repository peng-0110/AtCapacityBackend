package com.example.AtCapacity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Document(collection = "facilities")
public class Facility {

    /** Unique identifier for the facility */
    @Id
    private String id;
    
    /** Name of the facility (must be unique) */
    private String name;
    
    /** Current number of people in the facility */
    private int currentOccupancy;
    
    /** Maximum capacity of the facility */
    private int totalCapacity;
    
    /** Geographic location of the facility */
    private Location location;
    
    /** Owner/operator of the facility */
    private String owner;
    
    /** Operating hours in "HH:mm-HH:mm" format (e.g., "06:30-22:00") */
    private String hours;
    
    /** URL to facility's website or additional information */
    private String link;
    
    /** Type of facility (e.g., GYM, LIBRARY, etc.) */
    private FacilityType type;

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

    /**
     * Increments the current occupancy if below capacity.
     * @return true if successfully incremented, false if at capacity
     */
    public boolean incrementOccupancy() {
        return currentOccupancy < totalCapacity && ++currentOccupancy > 0;
    }

    /**
     * Decrements the current occupancy if above zero.
     * @return true if successfully decremented, false if already empty
     */
    public boolean decrementOccupancy() {
        return currentOccupancy > 0 && --currentOccupancy >= 0;
    }

    /**
     * Checks if the facility is at or exceeding capacity.
     * @return true if current occupancy >= total capacity
     */
    public boolean isFull() {
        return currentOccupancy >= totalCapacity;
    }

    /**
     * Calculates the current occupancy as a percentage of total capacity.
     * @return percentage between 0 and 100
     */
    public double getOccupancyPercentage() {
        return ((double) currentOccupancy / totalCapacity) * 100;
    }

    /**
     * Checks if the facility is currently open based on operating hours.
     * Hours must be in "HH:mm-HH:mm" format (e.g., "06:30-22:00")
     * @return true if current time is within operating hours
     */
    public boolean isOpen() {
        if (hours == null || hours.isEmpty()) {
            return false; // No hours specified
        }

        // Split the hours into opening and closing times
        String[] times = hours.split("-");
        if (times.length != 2) {
            return false; // Invalid format
        }

        try {
            LocalTime openingTime = LocalTime.parse(times[0]); // Parse opening time
            LocalTime closingTime = LocalTime.parse(times[1]); // Parse closing time
            LocalTime now = LocalTime.now(); // Get the current time

            // Check if the current time is within the range
            return !now.isBefore(openingTime) && !now.isAfter(closingTime);
        } catch (Exception e) {
            return false; // Return false if there's any parsing error
        }
    }

    /**
     * Calculates the distance between the facility and given coordinates.
     * Uses Haversine formula for great-circle distance.
     * @param lat latitude of the point to measure to
     * @param lon longitude of the point to measure to
     * @return distance in kilometers
     */
    public double calculateDistance(double lat, double lon) {
        // Haversine formula
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(this.location.getLatitude() - lat);
        double dLon = Math.toRadians(this.location.getLongitude() - lon);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(this.location.getLatitude())) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}