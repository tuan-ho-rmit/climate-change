package com.studio.climatechange.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studio.climatechange.viewModel.level2SubtaskB.Table;
import com.studio.climatechange.viewModel.level3SubtaskA.Region;
import com.studio.climatechange.viewModel.level3SubtaskBPart2.SimilarRegionsModelView;
import com.studio.climatechange.viewModel.level3SubtaskBPart2.SortView;

@Controller
public class SimilarRegionsController {

    private ArrayList<Region> convertStringToRegion(String regionName) {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region("Country", 1, true));
        regions.add(new Region("State", 2, false));
        regions.add(new Region("City", 3, false));

        for (Region region : regions) {
            if (region.getName().equals(regionName)) {
                region.setSelected(true);
            } else {
                region.setSelected(false);
            }
        }

        return regions;
    }

    private ArrayList<SortView> convertStringToSortView(String sortViewName) {
        ArrayList<SortView> sortViews = new ArrayList<>();
        sortViews.add(new SortView("Most Similar", 1, true));
        sortViews.add(new SortView("Least Similar", 2, false));

        for (SortView sortView : sortViews) {
            if (sortView.getName().equals(sortViewName)) {
                sortView.setSelected(true);
            } else {
                sortView.setSelected(false);
            }
        }

        return sortViews;
    }

    private static String generateQuery(String region, int startingYear, int period, String sortView,
            int resultNumber) {
        String selectedRegion;
        String selectedId;
        boolean includePopulation = "Country".equals(region); // Check if the region is a country

        if ("Country".equals(region)) {
            selectedRegion = "country";
            selectedId = "country_id";
        } else if ("City".equals(region)) {
            selectedRegion = "city";
            selectedId = "city_id";
        } else {
            selectedRegion = "state";
            selectedId = "state_id";
        }

        StringBuilder query = new StringBuilder("");

        return query.toString();
    }

    @GetMapping(value = { "/deep-dive/similar-regions" })
    public String level3SubtaskAPart2(
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "yearPeriod", required = false) String yearPeriod,
            @RequestParam(name = "startingYear", required = false) String startingYear,
            @RequestParam(name = "sortView", required = false) String sortView,
            @RequestParam(name = "numberResult", required = false) String numberResult,
            Model model) {
        {
            int parsedYearPeriod = 0;
            int parsedStartingYear = 0;
            int parsedNumberResult = 5;
            SimilarRegionsModelView modelView = new SimilarRegionsModelView();
            if (yearPeriod != null && !yearPeriod.isEmpty()) {
                try {
                    parsedYearPeriod = Integer.parseInt(yearPeriod);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (startingYear != null && !startingYear.isEmpty()) {
                try {
                    parsedStartingYear = Integer.parseInt(startingYear);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (numberResult != null && !numberResult.isEmpty()) {
                try {
                    parsedNumberResult = Integer.parseInt(numberResult);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            ArrayList<Region> regions = convertStringToRegion("Country");
            ArrayList<SortView> sortViews = convertStringToSortView("Most Similar");

            if (region != null) {
                regions = convertStringToRegion(region);
            }

            if (sortView != null) {
                sortViews = convertStringToSortView(sortView);
            }

            Table table = new Table(new String[] { "Name", "Temperature Changes" }, new String[][] {});

            modelView.setRegions(regions);
            modelView.setYearPeriod(parsedYearPeriod);
            modelView.setStartingYear(parsedStartingYear);
            modelView.setSortViews(sortViews);
            modelView.setResultNumber(parsedNumberResult);
            modelView.setTable(table);

            model.addAttribute("modelView", modelView);
            return "similarRegions";
        }
    }
}