package com.studio.climatechange.models;

import jakarta.persistence.*;

@Entity
@Table(name = "temperature")
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int year;

    @Column(nullable = true)
    private double averageTemperature;
    @Column(nullable = true)
    private double maximumTemperature;
    @Column(nullable = true)
    private double minimumTemperature;

    @ManyToOne
    @JoinColumn(name = "countryId", nullable = true)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "cityId", nullable = true)
    private City city;

    @ManyToOne
    @JoinColumn(name = "stateId", nullable = true)
    private State state;

    @ManyToOne
    @JoinColumn(name = "globalId", nullable = true)
    private Global global;

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
