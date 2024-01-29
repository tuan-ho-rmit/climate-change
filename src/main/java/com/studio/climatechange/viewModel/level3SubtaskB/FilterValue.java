package com.studio.climatechange.viewModel.level3SubtaskB;

public class FilterValue {
    private String name;
    private int id;
    private Boolean selected;

    public FilterValue(String name, int id, Boolean selected) {
        this.id = id;
        this.name= name;
        this.selected = selected;
    }
    // Getter for 'name'
    public String getName() {
        return name;
    }

    // Setter for 'name'
    public void setName(String name) {
        this.name = name;
    }

    // Getter for 'id'
    public int getId() {
        return id;
    }

    // Setter for 'id'
    public void setId(int id) {
        this.id = id;
    }

    // Getter for 'selected'
    public Boolean isSelected() {
        return selected;
    }

    // Setter for 'selected'
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
