package com.studio.climatechange.models;

import jakarta.persistence.*;

@Entity
@Table(name ="temperature")
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int year;

    private double averageTemperature;
    private double maximumTemperature;
    private double minimumTemperature;

    @ManyToOne
    @JoinColumn(name ="countryId", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name ="cityId", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name ="stateId", nullable = false)
    private State state;
    public int getId() {
        return id;
    }
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
}
