package com.studio.climatechange.viewModel.level2SubtaskA;



import java.util.ArrayList;

public class Level2SubtaskAViewModel {
    private ArrayList<Region> regions;
    private int startYears;
    private int endYears;
    private double averageTemperature;
    private double maxTemperature;
    private double minTemperature;
    private long Population;
    private Table table;
    private int Page;
    private int totalPage;
    private int pageSize;
    private String sortColumn;
    private String sortType;

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public void setStartYears(int startYears) {
        this.startYears = startYears;
    }

    public int getStartYears() {
        return startYears;
    }

    public void setEndYears(int endYears) {
        this.endYears = endYears;
    }

    public int getEndYears() {
        return endYears;
    }

    public void setAverageTemperature(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setPopulation(long population) {
        Population = population;
    }

    public long getPopulation() {
        return Population;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getPage() {
        return Page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }
}
