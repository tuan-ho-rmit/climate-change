package com.studio.climatechange.controller;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Cache.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.studio.climatechange.viewModel.level2SubtaskB.Table;
import com.studio.climatechange.viewModel.level3SubtaskA.Region;
import com.studio.climatechange.viewModel.level3SubtaskB.FilterValue;
import com.studio.climatechange.viewModel.level3SubtaskB.SimilarPeriodsModelView;
import com.studio.climatechange.viewModel.level3SubtaskB.ViewValue;

@Controller
public class SimilarPeriodsController {

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

    private ArrayList<FilterValue> convertStringToFilterValue(String filterId) {
        ArrayList<FilterValue> filterValues = new ArrayList<>();
        filterValues.add(new FilterValue("Absolute Values", 1, true));
        filterValues.add(new FilterValue("Relative Values", 2, false));

        for (FilterValue value : filterValues) {
            if (value.getName().equals(filterId)) {
                value.setSelected(true);
            } else {
                value.setSelected(false);
            }
        }
        return filterValues;

    }

    private static String generateQueryTable1(String region, int startingYear, int period, String regionName,
            String sortType) {
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
        query.append("WITH RegionTemp AS (")
                .append(" SELECT ")
                .append("    cn.name,")
                .append("    AVG(t.Average_temperature)  AS AvgTemp")
                .append(includePopulation ? ",AVG(p.population_number) AS population " : "")
                .append(" FROM ")
                .append("    temperature t")
                .append("    JOIN ").append(selectedRegion).append(" cn ON t.").append(selectedId).append(" = cn.id")
                .append(includePopulation
                        ? " JOIN population p ON t.year = p.Year AND p." + selectedId + " = t." + selectedId
                        : "")
                .append(" WHERE ")
                .append("    cn.name = '").append(regionName).append("' AND ")
                .append("    t.year BETWEEN ").append(startingYear).append(" AND ").append(startingYear + period)
                .append(" GROUP BY ")
                .append("    cn.name")
                .append("),")
                .append("SimilarTemp AS (")
                .append("    SELECT cn.name,")
                .append("           t.year AS StartYear,")
                .append("           AVG(t.Average_temperature) AS AvgTemp")
                .append(includePopulation ? ",AVG(p.population_number) AS population " : "")
                .append("      FROM temperature t")
                .append("           JOIN")
                .append("           ").append(selectedRegion).append(" cn ON t.").append(selectedId).append(" = cn.id")
                .append(includePopulation
                        ? " JOIN population p ON t.year = p.Year AND p." + selectedId + " = t." + selectedId
                        : "")
                .append("     WHERE cn.name = '").append(regionName).append("' ")
                .append("     GROUP BY cn.name,")
                .append("              t.year")
                .append("    HAVING t.year + ").append(period).append(" <= 2023")
                .append(")")
                .append("SELECT ")
                .append("	   CONCAT( st.StartYear, '-' , st.StartYear + ").append(period).append(" ) AS YearRange,")
                .append("       st.AvgTemp,")
                .append("       round(abs( rt.AvgTemp - st.AvgTemp),2) AS TempDifference,")
                .append("       round( ( round(( rt.AvgTemp - st.AvgTemp),2) / rt.AvgTemp * 100 ), 2 ) AS RelativeChangeTemp")
                .append(includePopulation ? ",round(st.population,0) AS Population ," : "")
                .append(includePopulation ? "round(abs(rt.population - st.population ),0  )   AS PopuDifference," : "")
                .append(includePopulation
                        ? "round( (round((rt.population - st.population),0) / rt.population * 100 ),2 )  AS RelativeChangePopu "
                        : "")
                .append(" FROM RegionTemp rt")
                .append("   JOIN")
                .append("        SimilarTemp st")
                .append(" ORDER BY TempDifference ")
                .append(sortType)
                .append(includePopulation ? "          ,PopuDifference ASC" : "")
                .append(" Limit 10 OFFSET 0;");

        System.err.println("query: " + query.toString());
        return query.toString();
    }

    private String[][] executeQueryTable1(String region, int startingYear, int period, String regionName,
            String sortType) {
        List<String[]> resultRows = new ArrayList<>();

        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = generateQueryTable1(region, startingYear, period, regionName, sortType);

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

    public String getRegionNamesJson(String region, String search) {
        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = "SELECT name FROM " + region +" WHERE name LIKE " +"'%" +search +"%'";
            System.err.println("sqlQuery: " + sqlQuery);
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery);
                    ResultSet resultSet = statement.executeQuery()) {

                // Create an ObjectMapper to convert ResultSet to JSON
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayNode jsonArray = objectMapper.createArrayNode();

                // Iterate through the ResultSet and add each name to the JSON array
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    jsonArray.add(name);
                }

                // Convert the JSON array to a string
                return jsonArray.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception according to your requirements
        }
        return "[]"; // Return an empty JSON array in case of an error
    }

    @GetMapping(value = { "/getListRegions" })
    @ResponseBody
    public String getListRegions(
        @RequestParam(name = "region", required = false) String region,
        @RequestParam(name = "search", required = false) String search
    ) {
        return getRegionNamesJson(region, search);
    }

    @GetMapping(value = { "/deep-dive/similar-periods" })
    public String level3SubtaskA(
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "yearPeriod", required = false) String yearPeriod,
            @RequestParam(name = "startingYear", required = false) String startingYear,
            @RequestParam(name = "regionName", required = false) String regionName,
            @RequestParam(name = "viewByTemperature", required = false) String viewByTemperature,
            @RequestParam(name = "viewByPopulation", required = false) String viewByPopulation,
            @RequestParam(name = "filterValue", required = false) String filterValue,
            @RequestParam(name = "viewValue", required = false) String viewValue,
            Model model) {
        int parsedYearPeriod = 0;
        int parsedStartingYear = 0;

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
        ArrayList<Region> regions = convertStringToRegion("Country");

        if (region != null) {
            regions = convertStringToRegion(region);
        }
        ArrayList<FilterValue> filterValues = convertStringToFilterValue(filterValue);
        SimilarPeriodsModelView table1 = new SimilarPeriodsModelView();
        boolean parsedViewByPopulation = "on".equals(viewByPopulation);
        ;
        boolean parsedViewByTemperature = "on".equals(viewByTemperature);
        ;

        Table table = new Table(new String[] { "Period", "Temperature", "Population" }, new String[][] {});
        table.setData(executeQueryTable1(region, parsedStartingYear, parsedYearPeriod, regionName, "ASC"));

        System.err.println("filterValue: " + filterValue);
        table1.setRegions(regions);
        table1.setYearPeriod(parsedYearPeriod);
        table1.setStartingYear(parsedStartingYear);
        table1.setRegionName(regionName);
        table1.setViewByPopulation(parsedViewByPopulation);
        table1.setViewByTemperature(parsedViewByTemperature);
        table1.setFilterValues(filterValues);
        table1.setTable(table);

        model.addAttribute("table1", table1);
        return "similarPeriods";
    }
}
