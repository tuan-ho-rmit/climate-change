package com.studio.climatechange.viewModel.level3SubtaskBPart2;

import java.util.ArrayList;

import com.studio.climatechange.viewModel.level3SubtaskA.Region;

public class Level3SubtaskBPart2ViewModel {
    private ArrayList<Region> regions;
    private int yearPeriod;
    private int startingYear;
    private int resultNumber;
    private ArrayList<SortView> sortViews;

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
