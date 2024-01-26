package com.studio.climatechange.dto;

public class PersonaDto {
    private String name;
    private String background;
    private String skillsAndExperiences;
    private String goals;
    private String needs;
    private String frustration;

    public String getName() {
        return name;
    }

    public String getSkillsAndExperiences() {
        return skillsAndExperiences;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getFrustration() {
        return frustration;
    }

    public void setFrustration(String frustration) {
        this.frustration = frustration;
    }

    public void setSkillsAndExperiences(String skillsAndExperiences) {
        this.skillsAndExperiences = skillsAndExperiences;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }
}
