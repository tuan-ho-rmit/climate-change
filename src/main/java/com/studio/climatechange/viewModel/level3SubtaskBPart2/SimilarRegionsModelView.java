package com.studio.climatechange.viewModel.level3SubtaskBPart2;

import java.util.ArrayList;

import com.studio.climatechange.viewModel.level2SubtaskB.Table;
import com.studio.climatechange.viewModel.level3SubtaskA.Region;

public class SimilarRegionsModelView {
    private ArrayList<Region> regions;
    private int yearPeriod;
    private int startingYear;
    private int resultNumber;
    private ArrayList<SortView> sortViews;
    private Table table;
    private String regionName;

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public int getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(int startingYear) {
        this.startingYear = startingYear;
    }

    public int getYearPeriod() {
        return yearPeriod;
    }

    public void setYearPeriod(int yearPeriod) {
        this.yearPeriod = yearPeriod;
    }

    public int getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(int resultNumber) {
        this.resultNumber = resultNumber;
    }

    public void setSortViews(ArrayList<SortView> sortViews) {
        this.sortViews = sortViews;
    }

    public ArrayList<SortView> getSortViews() {
        return sortViews;
    }
}
