package com.studio.climatechange.viewModel.level2SubtaskA;

public class Table {
    private String[] header;
    private String[][] data;

    public Table(String[] header, String[][] data){
        this.header = header;
        this.data = data;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[] getHeader() {
        return header;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public String[][] getData() {
        return data;
    }
}
