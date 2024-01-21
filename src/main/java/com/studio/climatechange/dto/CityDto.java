package com.studio.climatechange.dto;

import java.util.List;

public class CityDto {
    private int id;
    private String name;
    private double latitude;
    private double longitude;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<TemperatureDto> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<TemperatureDto> temperatures) {
        this.temperatures = temperatures;
    }
}
