package com.studio.climatechange.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "globalTemperature")
public class GlobalTemperature {
    @Id
    private int year;

    @Column(nullable = true)
    private double averageTemperature;
    @Column(nullable = true)
    private double maximumTemperature;
    @Column(nullable = true)
    private double minimumTemperature;

    @Column(nullable = true)
    private double landOceanAverageTemperature;
    @Column(nullable = true)
    private double landOceanMaximumTemperature;
    @Column(nullable = true)
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
