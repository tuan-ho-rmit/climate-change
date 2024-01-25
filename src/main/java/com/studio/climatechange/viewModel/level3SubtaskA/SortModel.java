package com.studio.climatechange.viewModel.level3SubtaskA;

enum SortOrder {
    ASC,
    DESC
}

public class SortModel {
    private SortOrder value;

    public SortOrder getValue() {
        return value;
    }

    public void setValue(SortOrder value) {
        this.value = value;
    }
}