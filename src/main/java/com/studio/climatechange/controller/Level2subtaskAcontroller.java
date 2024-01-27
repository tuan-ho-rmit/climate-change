package com.studio.climatechange.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


import com.studio.climatechange.viewModel.level2SubtaskA.Level2SubtaskAViewModel;
import com.studio.climatechange.viewModel.level2SubtaskA.Region;
import com.studio.climatechange.viewModel.level2SubtaskA.Table;


@Controller
public class Level2subtaskAcontroller {
    private Level2SubtaskAViewModel fakeData;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private ArrayList<Region> convertStringToRegion(String regionName) {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region(1, "Country", true));
        regions.add(new Region(2, "Global", false));
        for (Region region : regions) {
            if (region.getName().equals(regionName)) {
                region.setSelected(true);
            } else {
                region.setSelected(false);
            }
        }
        return regions;
    }

    public static String generateQuery(String region, int startYears, int endYears, int page, int pageSize) {
        String selectedRegion = null;
        String selectedId = null;

        if ("Country".equals(region)) {
            selectedRegion = "country";
            selectedId = "country_id";
        } else if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }
        String query ="WITH StarYear AS (" +
                "    SELECT c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature " +
                "    FROM temperature t " +
                "    JOIN  " + selectedRegion +
                "    c ON c.id = t. " + selectedId +
                "    JOIN population p ON c.id = p.country_id AND t.year = p.Year " +
                "    WHERE t.year =  " + startYears +
                "    GROUP BY c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature" +
                "), " +
                "EndYear AS (" +
                "    SELECT c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature " +
                "    FROM temperature t " +
                "    JOIN  " + selectedRegion +
                "    c ON c.id = t. " + selectedId +
                "    JOIN population p ON c.id = p.country_id AND t.year = p.Year " +
                "    WHERE t.year =  " + endYears +
                "    GROUP BY c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature" +
                ") " +
                "SELECT s.name AS Country, " +
                "       ROUND((e.average_temperature - s.average_temperature), 2) AS AvgDiff, " +
                "       ROUND((e.maximum_temperature - s.maximum_temperature), 2) AS MaxDiff, " +
                "       ROUND((e.minimum_temperature - s.minimum_temperature), 2) AS MinDiff, " +
                "       CAST((e.population_number - s.population_number) AS SIGNED) AS PopuDifference, " +
                "       CAST(((e.average_temperature - s.average_temperature) / (e.population_number - s.population_number)) * 100 AS SIGNED) AS Correlation " +
                "FROM StarYear s " +
                "JOIN EndYear e ON s.name = e.name" +
                " LIMIT " + pageSize + " " + "OFFSET " + ((page - 1) * pageSize);
        return query;
    }

    public String[][] executeQuery(String region, int startYears, int endYears, int page, int pageSize) {
        List<String[]> resultRows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = generateQuery(region, startYears, endYears, page, pageSize);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    // Create an array to store the current row's data
                    String[] row = new String[columnCount];

                    for (int i = 1; i <= columnCount; i++) {
                        // Retrieve data from each column and add it to the row array
                        row[i - 1] = resultSet.getString(i);
                    }

                    // Add the row to the resultRows list
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

    public static String countTotalPage(String region, int startYears, int endYears) {
        String selectedRegion = null;
        String selectedId = null;

        if ("Country".equals(region)) {
            selectedRegion = "country";
            selectedId = "country_id";
        } else if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }
        String query ="WITH StarYear AS (" +
                "    SELECT c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature " +
                "    FROM temperature t " +
                "    JOIN  " + selectedRegion +
                "    c ON c.id = t. " + selectedId +
                "    JOIN population p ON c.id = p.country_id AND t.year = p.Year " +
                "    WHERE t.year =  " + startYears +
                "    GROUP BY c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature" +
                "), " +
                "EndYear AS (" +
                "    SELECT c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature " +
                "    FROM temperature t " +
                "    JOIN  " + selectedRegion +
                "    c ON c.id = t. " + selectedId +
                "    JOIN population p ON c.id = p.country_id AND t.year = p.Year " +
                "    WHERE t.year =  " + endYears +
                "    GROUP BY c.name, t.year, p.population_number, t.maximum_temperature, t.minimum_temperature, t.average_temperature" +
                ") " +
                "SELECT COUNT(*)" +
                "FROM StarYear s " +
                "JOIN EndYear e ON s.name = e.name";
        return query;
    }

    public int executeCount(String region, int startYears, int endYears) {
        int result = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = countTotalPage(region, startYears, endYears);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    // Fetch the result as a String
                    result = resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }


    private Region findSelectedRegion(ArrayList<Region> regions) {
        for (Region region : regions) {
            if (region.getSelected()) {
                return region;
            }
        }
        return null; // Return null if no region is selected
    }

    @GetMapping (value = {"/high-level/subtask-a"})
    public String level2subtaskA(
            @RequestParam(name = "endYears", required = false) String endYears,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "startYears", required = false) String startYears,
            @RequestParam(name = "page", required = false) String page,
            Model model){

        ArrayList<Region> regions = convertStringToRegion(region);
        Level2SubtaskAViewModel modelView = new Level2SubtaskAViewModel();

        int parsedEndYears = 0;
        int parsedStartYears = 0;
        int parsedPage = 1;
        int pageSize = 10;

        if (endYears != null && !endYears.isEmpty()) {
            try {
                parsedEndYears = Integer.parseInt(endYears);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (startYears != null && !startYears.isEmpty()) {
            try {
                parsedStartYears = Integer.parseInt(startYears);
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

        String[] dynamicHeader = new String[]{"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature" , "Population", "Correlation"};


        String[][] data = executeQuery(region, parsedStartYears, parsedEndYears, parsedPage, pageSize);
        double totalPageDouble = (double) executeCount(region, parsedStartYears, parsedEndYears) / pageSize;

        int totalPage = (int) Math.ceil(totalPageDouble);

        Table table = new Table(dynamicHeader, data);


        modelView.setRegions(regions);
        modelView.setStartYears(parsedStartYears);
        modelView.setEndYears(parsedEndYears);
        modelView.setPage(parsedPage);
        modelView.setTable(table);
        modelView.setTotalPage(totalPage);

        Region selectedRegion = findSelectedRegion(regions);

        if (selectedRegion == null)
            selectedRegion = new Region(1, "Country", true);


        model.addAttribute("selectedRegion", selectedRegion);
        model.addAttribute("modelView", modelView);

        return "level2subtaskA";
    }
}
