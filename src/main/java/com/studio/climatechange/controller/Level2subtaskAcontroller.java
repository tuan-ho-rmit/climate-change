package com.studio.climatechange.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.studio.climatechange.viewModel.level3SubtaskA.Level3SubtaskAViewModel;
import com.studio.climatechange.viewModel.level3SubtaskA.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.studio.climatechange.repository.CountryRepository;
import com.studio.climatechange.services.impl.Level3SubstaskAService;
import com.studio.climatechange.viewModel.level2SubtaskA.Level2SubtaskAViewModel;
import com.studio.climatechange.viewModel.level2SubtaskA.Region;

@Controller
public class Level2subtaskAcontroller {
    //    private Level3subtaskAService level3SubtaskAService;
    private Level2SubtaskAViewModel fakeData;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
//    @Autowired
//    public Level2subtaskAcontroller(Level3SubstaskAService level3SubstaskAService, CountryRepository countryRepository) {
//        this.level3SubstaskAService = level3SubstaskAService;
//    }

    private ArrayList<Region> convertStringToRegion(String regionName) {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region(1, "Country", true));
        regions.add(new Region(2, "World", false));
        for (Region region : regions) {
            if (region.getName().equals(regionName)) {
                region.setSelected(true);
            } else {
                region.setSelected(false);
            }
        }
        return regions;
    }


    private String buildDynamicQuery(String region, int startYear, int endYear, int offset, int pageSize) {
//        String selectField;
//        String joinTable;
//        if (region.equals("Country")) {
//            selectField = "country.name";
//            joinTable = "country";
//        } else {
//            selectField = "global.name";
//            joinTable = "global";
//        }

        String query = "WITH StarYear AS ( " +
                "    SELECT " +
                "       c.name, " +
                "       t.year, " +
                "       p.population_number, " +
                "        t.average_temperature " +
                "    FROM temperature t " +
                "    JOIN country c ON c.id = t.country_id " +
                "    JOIN population p On c.id = p.country_id AND t.year = p.Year " +
                "    WHERE t.year = 1990 " +
                "    GROUP BY c.name, t.year, p.population_number, t.average_temperature " +
                "), " +
                "EndYear AS ( " +
                "   SELECT " +
                "       c.name, " +
                "       t.year, " +
                "       p.population_number, " +
                "        t.average_temperature " +
                "    FROM temperature  t " +
                "    JOIN country c ON c.id = t.country_id " +
                "    JOIN population p On c.id = p.country_id AND t.year = p.Year " +
                "    WHERE t.year = 2000 " +
                "    GROUP BY c.name, t.year, p.population_number, t.Average_temperature " +
                ") " +
                "SELECT " +
                "   s.name AS Country, " +
                "    s.year AS StartYear, " +
                "    s.Average_temperature AS StartTemp, " +
                "    s.population_number AS StartPopulation, " +
                "    e.year AS EndYear, " +
                "    e.Average_temperature AS EndTemp, " +
                "    e.population_number AS EndPopulation, " +
                "    (e.Average_temperature - s.Average_temperature) AS TempDifference, " +
                "    (e.population_number - s.population_number) AS PopuDifference, " +
                "    ( (e.Average_temperature - s.Average_temperature) / (e.population_number - s.population_number) ) * 100 AS Correlation " +
                "FROM StarYear s " +
                "JOIN EndYear e ON  s.name =  e.name";
        return query;

    }

    private String[][] executeQuery(String region , int startingYears, int endYears, int page, int pageSize){
        List<String[]> resultRows = new ArrayList<>();

        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = buildDynamicQuery(region, startingYears, endYears, page, pageSize);

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
            @RequestParam(name = "endYear", required = false) String endYear,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "startYear", required = false) String startYear,
            @RequestParam(name = "page", required = false) String page,
            Model model){

        ArrayList<Region> regions = convertStringToRegion(region);
        Level2SubtaskAViewModel modelView = new Level2SubtaskAViewModel();

        String[] dynamicHeader = new String [] {"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature", "Population", "Correlation"};
//        if (region.equals("Country")) {
//            dynamicHeader = new String[]{"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature", "Population", "Correlation"};
//        } else {
//            dynamicHeader = new String[]{"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature"};
//        }

        String[][] data = executeQuery(region, 1990, 2000, 10, 10);

        Table table = new Table(dynamicHeader, data);

        int parsedstartYear = Integer.parseInt(startYear);

        modelView.setRegions(regions);
        modelView.setStartYears(parsedstartYear);
//        modelView.setStartingYears(parsedStartingYears);
//        modelView.setMinAverageChange(parsedMinAverageChange);
//        System.err.println(parsedMinAverageChange);
//        modelView.setMaxAverageChange(parsedMaxAverageChange);
//        modelView.setMinPopulation(parsedMinPopulation);
//        modelView.setMaxPopulation(parsedMaxPopulation);
//        modelView.setPage(parsedPage);
//        modelView.setTotalPage(totalPage);
//        modelView.setTable(table);

//        System.out.println(buildDynamicQuery("Country", 1990, 2000, 10, 10));
        Region selectedRegion = findSelectedRegion(regions);

        if (selectedRegion == null)
            selectedRegion = new Region(1, "Country", true);

        model.addAttribute("selectedRegion", selectedRegion);
        model.addAttribute("modelView", modelView);

        return "level2subtaskA";
    }
}
