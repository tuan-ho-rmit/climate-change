package com.studio.climatechange.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.studio.climatechange.viewModel.level3SubtaskA.Region;
import com.studio.climatechange.viewModel.level3SubtaskBPart2.SortView;

@Controller
public class Level3SubtaskBPart2Controller {

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

    @GetMapping(value = { "/deep-dive/subtask-b-part-2" })
    public String level3SubtaskAPart2(
            Model model) {
        {

            return "level3SubtaskBPart2";
        }
    }
}