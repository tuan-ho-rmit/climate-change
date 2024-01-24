package com.studio.climatechange.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClimateChangeController {

    @GetMapping("/autocomplete/country")
    @ResponseBody
    public String autoCompleteCountry(@RequestParam("term") String term) {
        return autoComplete(term, "SELECT name FROM country WHERE name LIKE ?;");
    }

    @GetMapping("/autocomplete/year")
    @ResponseBody
    public String autoCompleteYear(@RequestParam("term") String term) {
        return autoComplete(term, "SELECT DISTINCT Year FROM temperature WHERE Year LIKE ?;");
    }

    @PostMapping(value = "/applyQuery")
    @ResponseBody
    public List<Table1> applyQuery(@RequestParam("Country") String value1,
                                   @RequestParam("StartYear") String value2,
                                   @RequestParam("EndYear") String value3,
                                   @RequestParam("colorRadio") String colorRadio) {

        List<Table1> retrievedData = new ArrayList<>();


        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/climatechange", "root", "123456789");
             PreparedStatement pst = con.prepareStatement(buildDynamicQuery(colorRadio))) {

            validateInputs(value1, value2, value3);

            pst.setString(1, value1);
            pst.setString(2, value2);
            pst.setString(3, value3);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                retrievedData.add(new Table1(rs.getString("name"), rs.getDouble("abs_avg_temperature_change"), rs.getDouble("abs_max_temperature_change"), rs.getDouble("abs_min_temperature_change")));
            }

            return retrievedData;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred", e);
        }
    }
    private String buildDynamicQuery(String colorRadio) {

        return "SELECT " +
                (colorRadio.equals("city") ? "city.name" : "country.name") + ", " +
                "ABS(AVG(t.average_temperature) - LAG(AVG(t.average_temperature), 1, 0) OVER (PARTITION BY YEAR(t.year) ORDER BY YEAR(t.year))) AS abs_avg_temperature_change, " +
                "ABS(MAX(t.maximum_temperature) - LAG(MAX(t.maximum_temperature), 1, 0) OVER (PARTITION BY YEAR(t.year) ORDER BY YEAR(t.year))) AS abs_max_temperature_change, " +
                "ABS(MIN(t.minimum_temperature) - LAG(MIN(t.minimum_temperature), 1, 0) OVER (PARTITION BY YEAR(t.year) ORDER BY YEAR(t.year))) AS abs_min_temperature_change " +
                "FROM city " +
                "INNER JOIN temperature AS t ON city.id = t.city_id " +
                "INNER JOIN country ON city.country_id = country.id " +
                "WHERE country.name = ? " +
                "AND t.year BETWEEN ? AND ? " +
                "GROUP BY " +
                (colorRadio.equals("city") ? "city.name" : "country.name") + ", YEAR(t.year) " +
                "ORDER BY " +
                (colorRadio.equals("city") ? "city.name" : "country.name") + ", YEAR(t.year)";
    }

    private String autoComplete(String term, String query) {
        List<String> list = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/climatechange", "root", "123456789");
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + term + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Gson().toJson(list);
    }

    private void validateInputs(String value1, String value2, String value3) throws SQLException {
        if (value1 == null || value1.trim().isEmpty()) {
            throw new SQLException("Country name cannot be empty.");
        }
        if (value2 == null || value2.trim().isEmpty()) {
            throw new SQLException("Start year cannot be empty.");
        }
        if (value3 == null || value3.trim().isEmpty()) {
            throw new SQLException("End year cannot be empty.");
        }
    }

    class Table1 {
        private String name;
        private double abs_avg_temperature_change;
        private double abs_max_temperature_change;
        private double abs_min_temperature_change;

        public Table1(String name, double abs_avg_temperature_change, double abs_max_temperature_change, double abs_min_temperature_change) {
            this.name = name;
            this.abs_avg_temperature_change = abs_avg_temperature_change;
            this.abs_max_temperature_change = abs_max_temperature_change;
            this.abs_min_temperature_change = abs_min_temperature_change;
        }

        public String getName() {
            return name;
        }

        public double getAbs_avg_temperature_change() {
            return abs_avg_temperature_change;
        }

        public double getAbs_max_temperature_change() {
            return abs_max_temperature_change;
        }

        public double getAbs_min_temperature_change() {
            return abs_min_temperature_change;
        }
    }

    @GetMapping(value = { "/LandingPage" })
    public String landingPage() {
        return "LandingPage";
    }

    @GetMapping(value = { "/Lv2-Subtask-B" })
    public String highlevelData() {
        return "Lv2-Subtask-B";
    }
}

