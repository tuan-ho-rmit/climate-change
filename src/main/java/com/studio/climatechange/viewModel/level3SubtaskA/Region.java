package com.studio.climatechange.viewModel.level3SubtaskA;

public class Region {
    private String name;
    private int id;
    private Boolean selected;

    public Region(String name, int id, Boolean selected) {
        this.name = name;
        this.id = id;
        this.selected = selected;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for selected
    public Boolean getSelected() {
        return selected;
    }

    // Setter for selected
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
