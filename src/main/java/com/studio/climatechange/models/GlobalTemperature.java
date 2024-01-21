package com.studio.climatechange.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="globalTemperature")
public class GlobalTemperature {
    @Id
    private int year;

    private double averageTemperature;
    private double maximumTemperature;
    private double minimumTemperature;

    private double landOceanAverageTemperature;
    private double landOceanMaximumTemperature;
    private double landOceanMinimumTemperature;

    public int getYear() {
        return year;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public double getMaximumTemperature() {
        return maximumTemperature;
    }

    public double getMinimumTemperature() {
        return minimumTemperature;
    }

    public double getLandOceanAverageTemperature() {
        return landOceanAverageTemperature;
    }

    public double getLandOceanMaximumTemperature() {
        return landOceanMaximumTemperature;
    }

    public double getLandOceanMinimumTemperature() {
        return landOceanMinimumTemperature;
    }
}
