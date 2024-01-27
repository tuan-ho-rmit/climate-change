package com.studio.climatechange.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="country")
public class Country {
    @Id
    private int id;

    private String name;
    private String countryCode;

    @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
    private Set<City> cities = new HashSet<>();

    @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
    private Set<State> states = new HashSet<>();

    @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
    private Set<Temperature> temperatures = new HashSet<>();
    @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
    private Set<Population> populations = new HashSet<>();
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
