package com.studio.climatechange.controller;

        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.ResponseBody;

        import java.sql.*;
        import java.util.HashMap;
        import java.util.Map;

@Controller
public class Level1SubtaskAController {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping(value = {"/home", "/"})
    public String landingPage() {
        return "LandingPage";  // Assuming this still renders a template
    }

    @GetMapping("/displayData")
    @ResponseBody
    public Map<String, Object> displayData() {
        try (Connection con = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = con.createStatement()) {

            String averageTemperatureEarliestYear = executeQueryAndGetFirstResult(stmt, "SELECT average_temperature FROM temperature WHERE year = (SELECT MIN(year) FROM temperature) AND global_id = 1");
            String averageTemperatureLatestYear = executeQueryAndGetFirstResult(stmt, "SELECT average_temperature FROM temperature WHERE year = (SELECT MAX(year) FROM temperature) AND global_id = 1");
            String earliestGlobalTemperatureYear = executeQueryAndGetFirstResult(stmt, "SELECT year FROM temperature WHERE year = (SELECT MIN(year) FROM temperature) AND global_id = 1");
            String latestGlobalTemperatureYear = executeQueryAndGetFirstResult(stmt, "SELECT year FROM temperature WHERE year = (SELECT MAX(year) FROM temperature) AND global_id = 1");
            String earliestGlobalTempYear = executeQueryAndGetFirstResult(stmt, "SELECT year FROM temperature WHERE year = (SELECT MIN(year) FROM temperature) AND global_id = 1");
            String latestGlobalTempYear = executeQueryAndGetFirstResult(stmt, "SELECT year FROM temperature WHERE year = (SELECT MAX(year) FROM temperature) AND global_id = 1");
            String earliestPopulationYear = executeQueryAndGetFirstResult(stmt, "SELECT MIN(year) FROM population WHERE country_id = 208");
            String latestPopulationYear = executeQueryAndGetFirstResult(stmt, "SELECT MAX(year) FROM population WHERE country_id = 208");
            String earliestPopYear = executeQueryAndGetFirstResult(stmt, "SELECT MIN(year) FROM population WHERE country_id = 208");
            String latestPopYear = executeQueryAndGetFirstResult(stmt, "SELECT MAX(year) FROM population WHERE country_id = 208");
            String earliestPopulationNumber = executeQueryAndGetFirstResult(stmt, "SELECT population_number FROM population WHERE year = (SELECT MIN(year) FROM population) AND country_id = 208");
            String latestPopulationNumber = executeQueryAndGetFirstResult(stmt, "SELECT population_number FROM population WHERE year = (SELECT MAX(year) FROM population) AND country_id = 208");
            String totalYearPop = executeQueryAndGetFirstResult(stmt, "SELECT COUNT(year) FROM population WHERE country_id = 208");
            String totalYearGlobal = executeQueryAndGetFirstResult(stmt, "SELECT COUNT(year) FROM temperature WHERE global_id = 1");

            Map<String, Object> data = new HashMap<>();

            data.put("earliestGlobalTemperatureYear", earliestGlobalTemperatureYear);
            data.put("latestGlobalTemperatureYear", latestGlobalTemperatureYear);
            data.put("earliestGlobalTempYear", earliestGlobalTempYear);
            data.put("latestGlobalTempYear", latestGlobalTempYear);
            data.put("averageTemperatureEarliestYear", averageTemperatureEarliestYear);
            data.put("averageTemperatureLatestYear", averageTemperatureLatestYear);
            data.put("earliestPopulationYear", earliestPopulationYear);
            data.put("latestPopulationYear", latestPopulationYear);
            data.put("latestPopYear", latestPopYear);
            data.put("earliestPopYear", earliestPopYear);
            data.put("earliestPopulationNumber", earliestPopulationNumber);
            data.put("latestPopulationNumber", latestPopulationNumber);
            data.put("totalYearPop", totalYearPop);
            data.put("totalYearGlobal", totalYearGlobal);

            return data;
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    private String executeQueryAndGetFirstResult(Statement stmt, String query) throws SQLException {
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getString(1);
        } else {
            return null;
        }
    }
}
