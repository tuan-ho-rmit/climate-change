package com.studio.climatechange.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="city")
public class City {
    @Id
    private int id;

    private String name;
    private String latitude;
    private String longitude;
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

    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
}
