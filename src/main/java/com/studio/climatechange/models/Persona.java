package com.studio.climatechange.models;

import jakarta.persistence.*;

@Entity
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String background;
    private String skillsAndExperiences;
    private String goals;
    private String needs;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getBackground() {
        return background;
    }

    public String getGoals() {
        return goals;
    }

    public String getNeeds() {
        return needs;
    }
}
