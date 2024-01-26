package com.studio.climatechange.models;

import jakarta.persistence.*;

@Entity
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String background;
    @Column(columnDefinition = "TEXT")
    private String skillsAndExperiences;
    @Column(columnDefinition = "TEXT")
    private String goals;
    @Column(columnDefinition = "TEXT")
    private String needs;
    @Column(columnDefinition = "TEXT")
    private String frustration;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
    public String getSkillsAndExperiences() {
        return skillsAndExperiences;
    }
    public String getBackground() {
        return background;
    }
    public String getFrustration() {
        return frustration;
    }

    public String getGoals() {
        return goals;
    }

    public String getNeeds() {
        return needs;
    }
}
