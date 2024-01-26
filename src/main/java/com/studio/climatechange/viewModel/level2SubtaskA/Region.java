package com.studio.climatechange.viewModel.level2SubtaskA;

public class Region {
    private int id;
    private String name;
    private Boolean selected;

    public Region(int id, String name, Boolean selected){
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }
}
