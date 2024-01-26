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

    @GetMapping(value = {"/LandingPage"})
    public String landingPage() {
        return "LandingPage";  // Assuming this still renders a template
    }

    @GetMapping("/displayData")
    @ResponseBody
    public Map<String, Object> displayData() {
        try (Connection con = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = con.createStatement()) {

            // Retrieve data using Statement (replace with prepared statements for security)
            String populationYearRange = executeQueryAndGetFirstResult(stmt, "SELECT MAX(year) - MIN(year) FROM population");
            String globalTemperatureYearRange = executeQueryAndGetFirstResult(stmt, "SELECT MAX(year) - MIN(year) FROM global_temperature");
            String earliestGlobalTemperatureYear = executeQueryAndGetFirstResult(stmt, "SELECT MIN(year) FROM global_temperature");
            String latestGlobalTemperatureYear = executeQueryAndGetFirstResult(stmt, "SELECT MAX(year) FROM global_temperature");
            String earliestPopulationYear = executeQueryAndGetFirstResult(stmt, "SELECT MIN(year) FROM population");
            String latestPopulationYear = executeQueryAndGetFirstResult(stmt, "SELECT MAX(year) FROM population");
            String averageTemperatureEarliestYear = executeQueryAndGetFirstResult(stmt, "SELECT average_temperature FROM global_temperature WHERE year = (SELECT MIN(year) FROM global_temperature)");
            String averageTemperatureLatestYear = executeQueryAndGetFirstResult(stmt, "SELECT average_temperature FROM global_temperature WHERE year = (SELECT MAX(year) FROM global_temperature)");

            Map<String, Object> data = new HashMap<>();

            data.put("populationYearRange", populationYearRange);
            data.put("globalTemperatureYearRange", globalTemperatureYearRange);
            data.put("earliestGlobalTemperatureYear", earliestGlobalTemperatureYear);
            data.put("latestGlobalTemperatureYear", latestGlobalTemperatureYear);
            data.put("earliestPopulationYear", earliestPopulationYear);
            data.put("latestPopulationYear", latestPopulationYear);
            data.put("averageTemperatureEarliestYear", averageTemperatureEarliestYear);
            data.put("averageTemperatureLatestYear", averageTemperatureLatestYear);

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
