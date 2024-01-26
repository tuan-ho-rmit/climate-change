package com.studio.climatechange.viewModel.level3SubtaskA;

public class Table {
    private String[] header;
    private String[][] data;

    public Table(String[] header, String[][] data) {
        this.header = header;
        this.data = data;
    }

    // Getter for header
    public String[] getHeader() {
        return header;
    }

    // Setter for header
    public void setHeader(String[] header) {
        this.header = header;
    }

    // Getter for data
    public String[][] getData() {
        return data;
    }

    // Setter for data
    public void setData(String[][] data) {
        this.data = data;
    }
}
