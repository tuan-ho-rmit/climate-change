package com.studio.climatechange.viewModel.level2SubtaskB;


import java.util.ArrayList;

public class Level2SubtaskBViewModel {

    private String country;
    private int startYear;
    private int endYear;
    private double minAverageChange;
    private double maxAverageChange;
    private Table table;
    private int page;
    private int pageSize;
    private int totalPage;


    public int getStartYear() { return startYear; }

    public void setStartYear(int StartYear) {
        this.startYear = startYear;
    }

    public int getEndYear() { return endYear; }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public double getMinAverageChange() {
        return minAverageChange;
    }

    public void setMinAverageChange(double minAverageChange) {
        this.minAverageChange = minAverageChange;
    }

    public double getMaxAverageChange() {
        return maxAverageChange;
    }

    public void setMaxAverageChange(double maxAverageChange) {
        this.maxAverageChange = maxAverageChange;
    }

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

