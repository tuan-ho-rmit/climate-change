package com.studio.climatechange.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double latitude;
    private double longitude;
    @ManyToOne
    @JoinColumn(name ="countryId", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.REMOVE)
    private Set<Temperature> temperatures = new HashSet<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}
