package com.studio.climatechange.controller;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

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

    private static String generateQuery(String region, int startingYear, int period, String regionName,String sortView,
            int resultNumber) {
        String selectedRegion;
        String selectedId;

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

        StringBuilder query = new StringBuilder("WITH CountryTemp AS ("
                + "SELECT "
                + selectedRegion + ".name, "
                + "AVG(CASE WHEN t.year = " + startingYear
                + " THEN t.Average_temperature ELSE NULL END) AS AvgTempStart, "
                + "AVG(CASE WHEN t.year = " + (startingYear + period)
                + " THEN t.Average_temperature ELSE NULL END) AS AvgTempEnd "
                + "FROM temperature t "
                + "JOIN " + selectedRegion + " ON t." + selectedId + " = " + selectedRegion + ".id "
                + "WHERE t.average_temperature IS NOT NULL "
                + "AND (t.year = " + startingYear + " OR t.year = " + (startingYear + period) + ") "
                + "GROUP BY " + selectedRegion + ".name), "
                + "RegionTempChange AS ("
                + "SELECT "
                + "( AvgTempEnd - AvgTempStart) AS RegionTempChange "
                + "FROM CountryTemp "
                + "WHERE name = '" + regionName + "') "
                + "SELECT "
                + "ct.name, "
                + "CONCAT('" + startingYear + "-" + (startingYear + period) + "') AS TimePeriods, "
                + "ABS(ROUND(( AvgTempEnd - AvgTempStart), 3)) AS TempDifference, "
                + "ABS(ROUND(( AvgTempEnd - AvgTempStart) - rtc.RegionTempChange, 3)) AS DiffFromRegionChange "
                + "FROM CountryTemp ct "
                + "CROSS JOIN RegionTempChange rtc "
                + "ORDER BY "
                + "CASE "
                + "WHEN ct.name = '" + regionName + "' THEN 0 "
                + "ELSE 1 "
                + "END, "
                + "DiffFromRegionChange " + sortView + " "
                + "LIMIT " + resultNumber);

                System.err.println(query.toString());
        return query.toString();
    }

    private String[][] executeQuery(String region, int startingYear, int period, String sortView,
            int resultNumber, String regionName) {
        List<String[]> resultRows = new ArrayList<>();
        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = generateQuery(region,startingYear, period, regionName,sortView, resultNumber);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    String[] row = new String[columnCount];

                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getString(i);
                    }

                    resultRows.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[][] resultArray = new String[resultRows.size()][];
        resultRows.toArray(resultArray);

        return resultArray;
    }

    @GetMapping(value = { "/deep-dive/similar-regions" })
    public String level3SubtaskAPart2(
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "yearPeriod", required = false) String yearPeriod,
            @RequestParam(name = "startingYear", required = false) String startingYear,
            @RequestParam(name = "sortView", required = false) String sortView,
            @RequestParam(name = "resultNumber", required = false) String numberResult,
            @RequestParam(name = "regionName", required = false) String regionName,
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

            String parsedSortView = "ASC";
            
            if (sortView != null) {
                sortViews = convertStringToSortView(sortView);
                if("Least Similar".equals(sortView)) {
                    parsedSortView = "DESC";
                } 
            }




            String [] [] data= executeQuery(region, parsedStartingYear, parsedYearPeriod,parsedSortView, parsedNumberResult, regionName);

            
            Table table = new Table(new String[] { "Name", "Temperature Changes" }, new String[][] {});
            table.setData(data);

            modelView.setRegions(regions);
            modelView.setYearPeriod(parsedYearPeriod);
            modelView.setStartingYear(parsedStartingYear);
            modelView.setSortViews(sortViews);
            modelView.setResultNumber(parsedNumberResult);
            modelView.setTable(table);
            modelView.setRegionName(regionName);
            
            model.addAttribute("modelView", modelView);
            return "similarRegions";
        }
    }
}