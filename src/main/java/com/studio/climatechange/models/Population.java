package com.studio.climatechange.models;

import jakarta.persistence.*;

@Entity
@Table(name ="population")
public class Population {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int year;

    @Column(nullable = true)
    private long populationNumber;

    @ManyToOne
    @JoinColumn(name ="countryId", nullable = false)
    private Country country;

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public long getPopulationNumber() {
        return populationNumber;
    }
}
