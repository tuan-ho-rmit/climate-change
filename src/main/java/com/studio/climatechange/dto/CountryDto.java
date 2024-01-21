package com.studio.climatechange.dto;

import com.studio.climatechange.models.City;
import com.studio.climatechange.models.Temperature;

import java.util.List;

public class CountryDto {
    private int id;
    private String name;
    private String countryCode;
    private List<CityDto> cities;
    private List<StateDto> states;
    private List<TemperatureDto> temperatures;
    private List<PopulationDto> populations;


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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
