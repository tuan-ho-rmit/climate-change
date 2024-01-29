package com.studio.climatechange.viewModel.level3SubtaskB;

import java.util.ArrayList;
import com.studio.climatechange.viewModel.level2SubtaskB.Table;
import com.studio.climatechange.viewModel.level3SubtaskA.Region;

public class SimilarPeriodsModelView {
    private ArrayList<Region> regions;
    private int yearPeriod;
    private int startingYear;
    private String regionName;
    private ArrayList<FilterValue> filterValues;
    private boolean viewByTemperature;
    private boolean viewByPopulation;
    private Table table;


    // Getter for 'regions'
    public ArrayList<Region> getRegions() {
        return regions;
    }

    // Setter for 'regions'
    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    // Getter for 'yearPeriod'
    public int getYearPeriod() {
        return yearPeriod;
    }

    // Setter for 'yearPeriod'
    public void setYearPeriod(int yearPeriod) {
        this.yearPeriod = yearPeriod;
    }

    // Getter for 'startingYear'
    public int getStartingYear() {
        return startingYear;
    }

    // Setter for 'startingYear'
    public void setStartingYear(int startingYear) {
        this.startingYear = startingYear;
    }

    // Getter for 'country'
    public String getRegionName() {
        return regionName;
    }

    // Setter for 'country'
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    // Getter for 'filterValue'
    public ArrayList<FilterValue> getFilterValues() {
        return filterValues;
    }

    // Setter for 'filterValue'
    public void setFilterValues(ArrayList<FilterValue> filterValues) {
        this.filterValues = filterValues;
    }

    // Getter for 'viewByTemperature'
    public boolean isViewByTemperature() {
        return viewByTemperature;
    }

    // Setter for 'viewByTemperature'
    public void setViewByTemperature(boolean viewByTemperature) {
        this.viewByTemperature = viewByTemperature;
    }

    // Getter for 'viewByPopulation'
    public boolean isViewByPopulation() {
        return viewByPopulation;
    }

    // Setter for 'viewByPopulation'
    public void setViewByPopulation(boolean viewByPopulation) {
        this.viewByPopulation = viewByPopulation;
    }

    // Getter for 'table'
    public Table getTable() {
        return table;
    }

    // Setter for 'table'
    public void setTable(Table table) {
        this.table = table;
    }
}
