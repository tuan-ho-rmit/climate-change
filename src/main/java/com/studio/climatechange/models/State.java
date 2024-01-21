package com.studio.climatechange.models;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="state")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name ="countryId", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "state", cascade = CascadeType.REMOVE)
    private Set<Temperature> temperatures = new HashSet<>();
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
