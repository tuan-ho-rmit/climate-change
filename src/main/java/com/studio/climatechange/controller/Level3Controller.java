package com.studio.climatechange.controller;

import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studio.climatechange.repository.CountryRepository;
import com.studio.climatechange.viewModel.level3SubtaskA.Level3SubtaskAViewModel;
import com.studio.climatechange.viewModel.level3SubtaskA.Region;
import com.studio.climatechange.viewModel.level3SubtaskA.Table;


@Controller
public class Level3Controller {
    private CountryRepository countryRepository;
    private Level3SubtaskAViewModel fakeData;
    private ArrayList<Region> regions;

    @Autowired
    public Level3Controller(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.fakeData = new Level3SubtaskAViewModel();
        generateFakeData();
    }

    private void generateFakeData() {

        // Generate fake data for regions
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region("Country", 1, true));
        regions.add(new Region("State", 2, false));
        regions.add(new Region("City", 3, false));
        // Set fake data to the view model
        fakeData.setRegions(regions);

        // Set other fake data to the view model
        fakeData.setYearPeriod(5);
        fakeData.setStartingYears(new int[] { 2010, 2015, 2020 });
        fakeData.setMinAverageChange(0.5);
        fakeData.setMaxAverageChange(2.0);
        fakeData.setMinPopulation(1000000);
        fakeData.setMaxPopulation(50000000);

        int[] startingYears = fakeData.getStartingYears();
        int period = fakeData.getYearPeriod();

        String[] dynamicHeader = new String[startingYears.length + 1];
        for (int i = 0; i < startingYears.length + 1; i++) {
            if (i == 0) {
                dynamicHeader[i] = "Name";
            } else {
                dynamicHeader[i] = (startingYears[i - 1]) + "-" + (startingYears[i - 1] + period - 1);
            }
        }
        // Generate fake data for the table
        String[][] data = {
                { "Country1", "30", "35", "40" },
                { "Country2", "29", "35", "40" },
                { "Country3", "33", "32", "37" },
                { "Country4", "30", "35", "40" },
                { "Country5", "29", "35", "40" },
                { "Country6", "31", "34", "39" },
                { "Country7", "32", "33", "38" },
                { "Country8", "28", "36", "41" },
                { "Country9", "33", "32", "37" },
                { "Country10", "34", "31", "36" },
                { "Country11", "27", "37", "42" },
                { "Country12", "36", "30", "35" },
                { "Country13", "26", "38", "43" },
                { "Country14", "37", "29", "34" },
                { "Country15", "25", "39", "44" }

        };
        Table table = new Table(dynamicHeader, data);
        // Set the table to the view model
        fakeData.setTable(table);

        // Set other fake data to the view model
        fakeData.setPage(1);
        fakeData.setPageSize(10);
        fakeData.setTotalPage(2);
    }

    @GetMapping(value = { "/deep-dive/subtask-a" })
    public String level3SubtaskA(
            @RequestParam(name = "yearPeriod", required = false) String yearPeriod,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "startingYears", required = false) String startingYears,
            @RequestParam(name = "minAverageChange", required = false) String minAverageChange,
            @RequestParam(name = "maxAverageChange", required = false) String maxAverageChange,
            @RequestParam(name = "minPopulation", required = false) String minPopulation,
            @RequestParam(name = "maxPopulation", required = false) String maxPopulation,
            Model model) {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region("Country", 1, true));
        regions.add(new Region("State", 2, false));
        regions.add(new Region("City", 3, false));


        

        System.err.println(region);
        model.addAttribute("fakeData", fakeData);
        return "level3SubtaskA";
    }
}
