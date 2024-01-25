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
                { "Country1", "30.2", "35.4", "40.2" },
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

        };
        Table table = new Table(dynamicHeader, data);
        // Set the table to the view model
        fakeData.setTable(table);

        // Set other fake data to the view model
        fakeData.setPage(1);
        fakeData.setPageSize(10);
        fakeData.setTotalPage(100);
    }

    private ArrayList<Region> convertStringToRegion(String regionName) {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region("Country", 1, false));
        regions.add(new Region("State", 2, false));
        regions.add(new Region("City", 3, false));

        for (Region region : regions) {
            if (region.getName().equals(regionName)) {
                region.setSelected(true);
            }
        }
        return regions;
    }
    private int[] parseStartingYears(String startingYears) {
        if (startingYears != null && !startingYears.isEmpty()) {
            String[] yearsArray = startingYears.split(",");
            int[] parsedYears = new int[yearsArray.length];
            
            for (int i = 0; i < yearsArray.length; i++) {
                try {
                    parsedYears[i] = Integer.parseInt(yearsArray[i].trim());
                } catch (NumberFormatException e) {
                    // Handle the case where a year is not a valid integer
                    e.printStackTrace();
                }
            }
            return parsedYears;
        } else {
            return new int[0]; // Return an empty array if startingYears is null or empty
        }
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
            @RequestParam(name = "page", required = false) String page,
            Model model) {
        int parsedYearPeriod = 0;
        if (yearPeriod != null && !yearPeriod.isEmpty()) {
            try {
                parsedYearPeriod = Integer.parseInt(yearPeriod);
            } catch (NumberFormatException e) {
                // Handle the case where yearPeriod is not a valid integer
                // You can log the error or take appropriate action
                e.printStackTrace();
            }
        }
        ArrayList<Region> regions = convertStringToRegion(region);

        double parsedMinAverageChange = 0.0;
        double parsedMaxAverageChange = 0.0;
        double parsedMinPopulation = 0.0;
        double parsedMaxPopulation = 0.0;
        int parsedPage = 1;
        int[] parsedStartingYears = parseStartingYears(startingYears);

        if (minAverageChange != null && !minAverageChange.isEmpty()) {
            try {
                parsedMinAverageChange = Double.parseDouble(minAverageChange);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }

        if (maxAverageChange != null && !maxAverageChange.isEmpty()) {
            try {
                parsedMaxAverageChange = Double.parseDouble(maxAverageChange);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }

        if (minPopulation != null && !minPopulation.isEmpty()) {
            try {
                parsedMinPopulation = Long.parseLong(minPopulation);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (maxPopulation != null && !maxPopulation.isEmpty()) {
            try {
                parsedMaxPopulation = Long.parseLong(maxPopulation);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (page != null && !page.isEmpty()) {
            try {
                parsedPage = Integer.parseInt(page);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        System.err.println(regions);
        System.err.println(startingYears);
        System.err.println("Parsed Year Period: " + parsedYearPeriod);
        System.err.println("Parsed Min Average Change: " + parsedMinAverageChange);
        System.err.println("Parsed Max Average Change: " + parsedMaxAverageChange);
        System.err.println("Parsed Min Population: " + parsedMinPopulation);
        System.err.println("Parsed Max Population: " + parsedMaxPopulation);
        System.err.println("page: " + parsedPage);
        System.err.println("starting years: " + parsedStartingYears);

        model.addAttribute("fakeData", fakeData);
        return "level3SubtaskA";
    }
}
