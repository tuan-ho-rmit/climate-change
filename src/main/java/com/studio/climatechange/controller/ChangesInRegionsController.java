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

import com.studio.climatechange.viewModel.level3SubtaskA.ChangesInRegionsModelView;
import com.studio.climatechange.viewModel.level3SubtaskA.Region;
import com.studio.climatechange.viewModel.level3SubtaskA.Table;

@Controller
public class ChangesInRegionsController {
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

    private Region findSelectedRegion(ArrayList<Region> regions) {
        for (Region region : regions) {
            if (region.getSelected()) {
                return region;
            }
        }
        return null; // Return null if no region is selected
    }

    private static String generateQuery(String region, int[] startingYears, int period, double minAverageChange,
            double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize,
            String regionName, String sortType, String sortColumn) {
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

        if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }

        String parseSortColumn = "";
        int intSortColumn = 0;
        try {

            if (sortColumn != null) {
                intSortColumn = Integer.parseInt(sortColumn);
                int inputSortColumn = intSortColumn + 1;

                parseSortColumn = "Table" + sortColumn + ".avg" + inputSortColumn;
                System.err.println("parsedSortColumn: " + parseSortColumn);
            }
        } catch (NumberFormatException e) {
            // Handle the case where the input cannot be parsed to an integer
            System.err.println("Error parsing sortColumn: " + e.getMessage());
            // You may want to log the error, throw an exception, or take appropriate action
        }

        StringBuilder query = new StringBuilder("WITH ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("Table").append(i).append(" AS (")
                    .append("SELECT ").append(selectedRegion).append(".name, ")
                    .append("AVG(t.average_temperature) AS avg").append(i + 1).append(" ");
            if ("Country".equals(region)) {
                query.append(", AVG(p.population_number) AS population").append(" ");
            }
            ;

            query.append("FROM temperature t ")
                    .append("JOIN ").append(selectedRegion).append(" ON ").append(selectedRegion)
                    .append(".id = t.").append(selectedId).append(" ");
            if ("Country".equals(region)) {
                query.append("LEFT JOIN population p ON t.year = p.year AND t.").append(selectedId)
                        .append(" = p.").append(selectedId).append(" ");
            }
            ;
            query.append("WHERE t.Year BETWEEN ").append(startingYears[i]).append(" AND ")
                    .append(startingYears[i] + period).append(" ")
                    .append("GROUP BY ").append(selectedRegion).append(".name), ");
        }

        query.delete(query.length() - 2, query.length());

        query.append(", CountryData AS ( ");
        query.append("SELECT ")
                .append("Table0.name ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append(", Table").append(i).append(".avg").append(i + 1).append(" ");
        }

        query.append("FROM Table0");

        for (int i = 1; i < startingYears.length; i++) {
            query.append(" JOIN Table").append(i)
                    .append(" ON Table0.name = Table").append(i).append(".name");
        }

        query.append(" WHERE ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("ABS(").append("Table").append(i).append(".avg").append(i + 1).append(")").append(" >= 1 ")
                    .append(" And ");

            if ("Country".equals(region)) {
                if (maxPopulation > 0) {
                    query.append("(Table").append(i).append(".Population IS NULL OR Table").append(i)
                            .append(".Population BETWEEN ")
                            .append(minPopulation).append(" AND ").append(maxPopulation).append(") ");
                } else {
                    query.append("COALESCE(Table").append(i).append(".Population, 0) >= 0 ");
                }

                if (i < startingYears.length - 1) {
                    query.append("AND ");
                }
            }
        }
        if ("Country".equals(region)) {
        } else {
            query = new StringBuilder(query.substring(0, query.length() - 4));
        }
        for (int i = 1; i < startingYears.length; i++) {
            query.append(" AND ").append("ABS(").append("Table").append(i).append(".avg").append(i + 1)
                    .append("-").append("Table0.avg1").append(")").append(" BETWEEN ").append(minAverageChange)
                    .append(" AND ").append(maxAverageChange);
        }

        query.append(" AND Table0.name = '").append(regionName).append("'");

        query.append(")");

        query.append("SELECT ")
                .append("Table0.name ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append(", Table").append(i).append(".avg").append(i + 1).append(" AS \"").append(startingYears[i])
                    .append("-").append(startingYears[i] + period).append("\" ");
        }

        query.append("FROM Table0");

        for (int i = 1; i < startingYears.length; i++) {
            query.append(" JOIN Table").append(i)
                    .append(" ON Table0.name = Table").append(i).append(".name");
        }

        query.append(" WHERE ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("ABS(").append("Table").append(i).append(".avg").append(i + 1).append(")").append(" >= 1 ")
                    .append(" And ");

            if ("Country".equals(region)) {
                if (maxPopulation > 0) {
                    query.append("(Table").append(i).append(".Population IS NULL OR Table").append(i)
                            .append(".Population BETWEEN ")
                            .append(minPopulation).append(" AND ").append(maxPopulation).append(") ");
                } else {
                    query.append("COALESCE(Table").append(i).append(".Population, 0) >= 0 ");
                }

                if (i < startingYears.length - 1) {
                    query.append("AND ");
                }
            }
        }
        if ("Country".equals(region)) {
        } else {
            query = new StringBuilder(query.substring(0, query.length() - 4));
        }
        for (int i = 1; i < startingYears.length; i++) {
            query.append(" AND ").append("ABS(").append("Table").append(i).append(".avg").append(i + 1)
                    .append("-").append("( Select ").append("avg").append(i + 1).append(" from CountryData ")
                    .append(")")
                    .append(")").append(" BETWEEN ").append(minAverageChange)
                    .append(" AND ").append(maxAverageChange);
        }

        query.append(" AND Table0.name <> '").append(regionName).append("'");

        if (parseSortColumn != null && !parseSortColumn.isEmpty() && sortType != null && !sortType.isEmpty()) {
            query.append(" ORDER BY (").append(parseSortColumn).append(" - ").append(" (SELECT avg")
                    .append(intSortColumn + 1).append(" FROM CountryData)").append(")")
                    .append(" ").append(sortType);
        }
        query.append(" LIMIT ").append(pageSize).append(" ").append("OFFSET ").append((page - 1) * pageSize);
        System.err.println(query.toString());
        return query.toString();
    }

    private static String generateQueryRegion(String region, int[] startingYears, int period, double minAverageChange,
            double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize,
            String regionName) {
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

        if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }

        StringBuilder query = new StringBuilder("WITH ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("Table").append(i).append(" AS (")
                    .append("SELECT ").append(selectedRegion).append(".name, ")
                    .append("AVG(t.average_temperature) AS avg").append(i + 1).append(" ");
            if ("Country".equals(region)) {
                query.append(", AVG(p.population_number) AS population").append(" ");
            }
            ;

            query.append("FROM temperature t ")
                    .append("JOIN ").append(selectedRegion).append(" ON ").append(selectedRegion)
                    .append(".id = t.").append(selectedId).append(" ");
            if ("Country".equals(region)) {
                query.append("LEFT JOIN population p ON t.year = p.year AND t.").append(selectedId)
                        .append(" = p.").append(selectedId).append(" ");
            }
            ;
            query.append("WHERE t.Year BETWEEN ").append(startingYears[i]).append(" AND ")
                    .append(startingYears[i] + period).append(" ")
                    .append("GROUP BY ").append(selectedRegion).append(".name), ");
        }

        query.delete(query.length() - 2, query.length());

        query.append("SELECT ")
                .append("Table0.name ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append(", Table").append(i).append(".avg").append(i + 1).append(" AS \"").append(startingYears[i])
                    .append("-").append(startingYears[i] + period).append("\" ");
        }

        query.append("FROM Table0");

        for (int i = 1; i < startingYears.length; i++) {
            query.append(" JOIN Table").append(i)
                    .append(" ON Table0.name = Table").append(i).append(".name");
        }

        query.append(" WHERE ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("ABS(").append("Table").append(i).append(".avg").append(i + 1).append(")").append(" >= 1 ")
                    .append(" And ");

            if ("Country".equals(region)) {
                if (maxPopulation > 0) {
                    query.append("(Table").append(i).append(".Population IS NULL OR Table").append(i)
                            .append(".Population BETWEEN ")
                            .append(minPopulation).append(" AND ").append(maxPopulation).append(") ");
                } else {
                    query.append("COALESCE(Table").append(i).append(".Population, 0) >= 0 ");
                }

                if (i < startingYears.length - 1) {
                    query.append("AND ");
                }
            }
        }
        if ("Country".equals(region)) {
        } else {
            query = new StringBuilder(query.substring(0, query.length() - 4));
        }
        for (int i = 1; i < startingYears.length; i++) {
            query.append(" AND ").append("ABS(").append("Table").append(i).append(".avg").append(i + 1)
                    .append("-").append("Table0.avg1").append(")").append(" BETWEEN ").append(minAverageChange)
                    .append(" AND ").append(maxAverageChange);
        }

        if (regionName != null) {

            query.append(" AND Table0.name = '").append(regionName).append("'");
        }
        return query.toString();
    }

    public String[] executeQueryRegion(String region, int[] startingYears, int period, double minAverageChange,
            double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize,
            String regionName) {
        List<String> resultList = new ArrayList<>();

        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = generateQueryRegion(region, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation, page, pageSize, regionName);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    // Concatenate values of each column into a single string, separated by a
                    // delimiter
                    StringBuilder row = new StringBuilder();

                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) {
                            row.append(", "); // Use a comma and space as a delimiter
                        }
                        // Append the value to the row string
                        row.append(resultSet.getString(i));
                    }

                    // Add the concatenated row to the resultList
                    resultList.add(row.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert the List<String> to a String[]
        String[] resultArray = resultList.toArray(new String[0]);

        if (resultArray.length > 0) {
            String[] output = resultArray[0].split(",");
            return output;
        } else {
            // Handle the case where the resultArray is empty (no results from the query)
            return new String[0]; // Or return an error message or handle it according to your needs
        }
    }

    public static String countTotalPage(String region, int[] startingYears, int period, double minAverageChange,
            double maxAverageChange, long minPopulation, long maxPopulation, String regionName) {
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
        if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }

        StringBuilder query = new StringBuilder("WITH ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("Table").append(i).append(" AS (")
                    .append("SELECT ").append(selectedRegion).append(".name, ")
                    .append("AVG(t.average_temperature) AS avg").append(i + 1).append(" ");
            if ("Country".equals(region)) {
                query.append(", AVG(p.population_number) AS population").append(" ");
            }
            ;

            query.append("FROM temperature t ")
                    .append("JOIN ").append(selectedRegion).append(" ON ").append(selectedRegion)
                    .append(".id = t.").append(selectedId).append(" ");
            if ("Country".equals(region)) {
                query.append("LEFT JOIN population p ON t.year = p.year AND t.").append(selectedId)
                        .append(" = p.").append(selectedId).append(" ");
            }
            ;
            query.append("WHERE t.Year BETWEEN ").append(startingYears[i]).append(" AND ")
                    .append(startingYears[i] + period).append(" ")
                    .append("GROUP BY ").append(selectedRegion).append(".name), ");
        }

        query.delete(query.length() - 2, query.length());

        query.append("SELECT ")
                .append("count(*) ");

        query.append("FROM Table0");

        for (int i = 1; i < startingYears.length; i++) {
            query.append(" JOIN Table").append(i)
                    .append(" ON Table0.name = Table").append(i).append(".name");
        }

        query.append(" WHERE ");

        for (int i = 0; i < startingYears.length; i++) {
            query.append("Table").append(i).append(".avg").append(i + 1).append(" >= 1 ").append(" AND ");

            if ("Country".equals(region)) {
                if (maxPopulation > 0) {
                    query.append("(Table").append(i).append(".Population IS NULL OR Table").append(i)
                            .append(".Population BETWEEN ")
                            .append(minPopulation).append(" AND ").append(maxPopulation).append(") ");
                } else {
                    query.append("COALESCE(Table").append(i).append(".Population, 0) >= 0 ");
                }

                if (i < startingYears.length - 1) {
                    query.append("AND ");
                }
            }
        }
        if ("Country".equals(region)) {
        } else {
            query = new StringBuilder(query.substring(0, query.length() - 4));
        }
        for (int i = 1; i < startingYears.length; i++) {
            query.append(" AND ").append("ABS(").append("Table").append(i).append(".avg").append(i + 1)
                    .append("-").append("Table0.avg1").append(")").append(" BETWEEN ").append(minAverageChange)
                    .append(" AND ").append(maxAverageChange);
        }
        return query.toString();
    }

    public int executeCount(String region, int[] startingYears, int period, double minAverageChange,
            double maxAverageChange, long minPopulation, long maxPopulation, String regionName) {
        int result = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = countTotalPage(region, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation, regionName);

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

    public String[][] executeQuery(String region, int[] startingYears, int period, double minAverageChange,
            double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize,
            String regionName, String sortType, String sortColumn) {
        List<String[]> resultRows = new ArrayList<>();

        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sqlQuery = generateQuery(region, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation, page, pageSize, regionName, sortType, sortColumn);

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

    @GetMapping(value = { "/deep-dive/changes-in-regions" })
    public String level3SubtaskA(
            @RequestParam(name = "yearPeriod", required = false) String yearPeriod,
            @RequestParam(name = "regionName", required = false) String regionName,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "startingYears", required = false) String startingYears,
            @RequestParam(name = "minAverageChange", required = false) String minAverageChange,
            @RequestParam(name = "maxAverageChange", required = false) String maxAverageChange,
            @RequestParam(name = "minPopulation", required = false) String minPopulation,
            @RequestParam(name = "maxPopulation", required = false) String maxPopulation,
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "sortColumn", required = false) String sortColumn,
            @RequestParam(name = "sortType", required = false) String sortType,
            Model model) {

        int parsedYearPeriod = 0;
        int pageSize = 10;
        if (yearPeriod != null && !yearPeriod.isEmpty()) {
            try {
                parsedYearPeriod = Integer.parseInt(yearPeriod);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Region> regions = convertStringToRegion(region);
        ChangesInRegionsModelView modelView = new ChangesInRegionsModelView();
        double parsedMinAverageChange = 0.0;
        double parsedMaxAverageChange = 0.0;
        long parsedMinPopulation = 0;
        long parsedMaxPopulation = 0;
        int parsedPage = 1;

        int[] parsedStartingYears = parseStartingYears(startingYears);
        // if (startingYears != null && !startingYears.isEmpty()) {
        // try {
        // parsedStartingYears = parseStartingYears(startingYears);
        // } catch (NumberFormatException e) {
        // e.printStackTrace();
        // }
        // }

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
        String[] dynamicHeader = new String[parsedStartingYears.length + 1];
        for (int i = 0; i < parsedStartingYears.length + 1; i++) {
            if (i == 0) {
                dynamicHeader[i] = "Name";
            } else {
                dynamicHeader[i] = (parsedStartingYears[i - 1]) + "-"
                        + (parsedStartingYears[i - 1] + parsedYearPeriod);
            }
        }
        String[][] data = { {} };
        if (regionName != null) {
            data = executeQuery(region, parsedStartingYears, parsedYearPeriod,
                    parsedMinAverageChange,
                    parsedMaxAverageChange, parsedMinPopulation, parsedMaxPopulation, parsedPage,
                    pageSize, regionName, sortType, sortColumn);
        }
        double totalPageDouble = 0;

        if (regionName != null) {
            totalPageDouble = executeCount(region, parsedStartingYears,
                    parsedYearPeriod, parsedMinAverageChange,
                    parsedMaxAverageChange, parsedMinPopulation, parsedMaxPopulation, regionName) / pageSize;
        }

        String[] firstRow = new String[] {};
        if (regionName != null) {
            firstRow = executeQueryRegion(region, parsedStartingYears, parsedYearPeriod,
                    parsedMinAverageChange,
                    parsedMaxAverageChange, parsedMinPopulation, parsedMaxPopulation, parsedPage,
                    pageSize, regionName);
        }

        int totalPage = (int) Math.ceil(totalPageDouble);

        Table table = new Table(dynamicHeader, data);

        modelView.setRegions(regions);
        modelView.setYearPeriod(parsedYearPeriod);
        modelView.setStartingYears(parsedStartingYears);
        modelView.setMinAverageChange(parsedMinAverageChange);
        System.err.println(parsedMinAverageChange);
        modelView.setMaxAverageChange(parsedMaxAverageChange);
        modelView.setMinPopulation(parsedMinPopulation);
        modelView.setMaxPopulation(parsedMaxPopulation);
        modelView.setPage(parsedPage);
        modelView.setTotalPage(totalPage);
        modelView.setTable(table);
        modelView.setRegionName(regionName);

        if ("ASC".equals(sortType)) {
            System.err.println("ASC");
            model.addAttribute("nextSortType", "DESC");

        } else if ("DESC".equals(sortType)) {
            System.err.println("DESC");

            model.addAttribute("nextSortType", "");

        } else {
            System.err.println("null");

            model.addAttribute("nextSortType", "ASC");
        }

        Region selectedRegion = findSelectedRegion(regions);

        if (selectedRegion == null)
            selectedRegion = new Region("Country", 1, true);

        modelView.setSortColumn((sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "");
        modelView.setSortType((sortType != null && !sortType.isEmpty()) ? sortType : "");
        model.addAttribute("selectedRegion", selectedRegion);
        model.addAttribute("modelView", modelView);
        model.addAttribute("firstRow", firstRow);
        return "changesInRegions";
    }
}
