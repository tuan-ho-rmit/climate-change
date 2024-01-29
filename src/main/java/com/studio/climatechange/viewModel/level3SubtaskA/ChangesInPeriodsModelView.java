package com.studio.climatechange.viewModel.level3SubtaskA;

import java.util.ArrayList;


public class ChangesInPeriodsModelView {
    private ArrayList<Region> regions;
    private int yearPeriod;
    private int[] startingYears;
    private double minAverageChange;
    private double maxAverageChange;
    private long minPopulation;
    private long maxPopulation;
    private Table table;
    private int page;
    private int pageSize;
    private int totalPage;
    private String sortColumn;
    private String sortType;

    public String getSortColumn() {
        return sortColumn;
    }
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
    public String getSortType() {
        return sortType;
    }
 
    // Getter and setter for 'regions'
    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    // Getter and setter for 'yearPeriod'
    public int getYearPeriod() {
        return yearPeriod;
    }

    public void setYearPeriod(int yearPeriod) {
        this.yearPeriod = yearPeriod;
    }

    // Getter and setter for 'startingYears'
    public int[] getStartingYears() {
        return startingYears;
    }

    public void setStartingYears(int[] startingYears) {
        this.startingYears = startingYears;
    }

    // Getter and setter for 'minAverageChange'
    public double getMinAverageChange() {
        return minAverageChange;
    }

    public void setMinAverageChange(double minAverageChange) {
        this.minAverageChange = minAverageChange;
    }

    // Getter and setter for 'maxAverageChange'
    public double getMaxAverageChange() {
        return maxAverageChange;
    }

    public void setMaxAverageChange(double maxAverageChange) {
        this.maxAverageChange = maxAverageChange;
    }

    // Getter and setter for 'minPopulation'
    public long getMinPopulation() {
        return minPopulation;
    }

    public void setMinPopulation(long minPopulation) {
        this.minPopulation = minPopulation;
    }

    // Getter and setter for 'maxPopulation'
    public long getMaxPopulation() {
        return maxPopulation;
    }

    public void setMaxPopulation(long maxPopulation) {
        this.maxPopulation = maxPopulation;
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

    public int getTotalPage() {
        return totalPage;
    }
}

