package com.example.AtCapacity.model;

public class Location {
    private String street;
    private String city;
    private String postal;
    private double latitude;
    private double longitude;

    // Constructors
    public Location() {
    }

    public Location(String street, String city, String postal, double latitude, double longitude) {
        this.street = street;
        this.city = city;
        this.postal = postal;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}