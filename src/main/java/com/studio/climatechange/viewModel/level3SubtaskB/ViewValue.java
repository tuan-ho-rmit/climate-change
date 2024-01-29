package com.studio.climatechange.viewModel.level3SubtaskB;

public class ViewValue {
    private String name;
    private int id;
    private Boolean selected;

    public ViewValue(String name, int id, Boolean selected) {
        this.id = id;
        this.name= name;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getSelected() {
        return selected;
    }
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
