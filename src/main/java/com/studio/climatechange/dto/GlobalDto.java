package com.studio.climatechange.dto;

import java.util.List;

public class GlobalDto {
    private int id;
    private String name;

    private List<TemperatureDto> temperatures;
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

    public List<TemperatureDto> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<TemperatureDto> temperatures) {
        this.temperatures = temperatures;
    }
}
