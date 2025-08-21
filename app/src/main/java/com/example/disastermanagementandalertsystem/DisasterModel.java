package com.example.disastermanagementandalertsystem;

public class DisasterModel {
    private String name;
    private String description;
    private String alertLevel;
    private String severity;
    private double latitude;
    private double longitude;
    private int population;

    public DisasterModel(String name, String description, String alertLevel, String severity,
                         double latitude, double longitude, int population) {
        this.name = name;
        this.description = description;
        this.alertLevel = alertLevel;
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAlertLevel() { return alertLevel; }
    public String getSeverity() { return severity; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getPopulation() { return population; }
}

