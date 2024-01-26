package com.studio.climatechange.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "global")
public class Global {
    @Id
    private int id;
    private String name;
    @OneToMany(mappedBy = "global", cascade = CascadeType.REMOVE)
    private Set<Temperature> temperatures = new HashSet<>();
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
