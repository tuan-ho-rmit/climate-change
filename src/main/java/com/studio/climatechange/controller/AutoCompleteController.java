package com.example.climatedataweb;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AutoCompleteController {

    @GetMapping("/autocomplete/country")
    public String autoCompleteCountry(@RequestParam("term") String term) {
        return autoComplete(term, "SELECT name FROM country WHERE name LIKE ?;");
    }

    @GetMapping("/autocomplete/year")
    public String autoCompleteYear(@RequestParam("term") String term) {
        return autoComplete(term, "SELECT DISTINCT Year FROM temperature WHERE Year LIKE ?;");
    }

    private String autoComplete(String term, String query) {
        List<String> list = new ArrayList<>();

        // Database connection and querying
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/climatechange", "root", "123456789");
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + term + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }

        // Convert list to JSON
        return new Gson().toJson(list);
    }

    @GetMapping("/LandingPage")
    public String LandingPage() {
        return "LandingPage";
    }

    @GetMapping("/Lv2-Subtask-B")
    public String HighlevelData() {
        return "Lv2-Subtask-B";
    }
}

