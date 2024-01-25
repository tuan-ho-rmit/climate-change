package com.studio.climatechange.viewModel.level2SubtaskB;


import java.util.ArrayList;

public class Level2SubtaskBViewModel {

    private String country;
    private int startYear;
    private int endYear;
    private double minTemperatureChange;
    private double maxTemperatureChange;
    private double averageTemperatureChange;
    private Table table;
    private int page;
    private int pageSize;
    private int totalPage;



    public String getCountry() { return country; }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStartYear() { return startYear; }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() { return endYear; }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public double getMinTemperatureChange() {
        return minTemperatureChange;
    }

    public void setMinTemperatureChange(double minTemperatureChange) { this.minTemperatureChange = minTemperatureChange; }

    public double getMaxTemperatureChange() {
        return maxTemperatureChange;
    }

    public void setMaxTemperatureChangeChange(double maxTemperatureChange) { this.maxTemperatureChange = maxTemperatureChange; }

    public double getAverageTemperatureChange() {
        return averageTemperatureChange;
    }

    public void setAverageTemperatureChangeChange(double averageTemperatureChange) { this.averageTemperatureChange = averageTemperatureChange; }

    // Getter and setter for 'table'
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    // Getter and setter for 'page'
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    // Getter and setter for 'pageSize'
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}

