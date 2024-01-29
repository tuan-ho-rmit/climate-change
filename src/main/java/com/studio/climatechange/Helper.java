package com.studio.climatechange;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Helper implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
    private static final int CHUNK_SIZE = 10000;
    // Method to be executed after the Spring context is initialized
    @Override
    public void run(String... args) throws Exception {
        List<String> csvFiles = List.of("Student.csv", "Persona.csv", "Country.csv", "City.csv", "State.csv", "Population.csv", "Global.csv", "Temperature.csv");
       List<String> tableNames = List.of("Student", "Persona", "Country", "City", "State", "Population", "Global", "Temperature");

        for (int i = 0; i < csvFiles.size(); i++) {
            if (!isTablePopulated(tableNames.get(i))) {
                if (!csvFiles.get(i).equals("Temperature.csv")) {
                    insertCsvIntoTable(csvFiles.get(i), tableNames.get(i));
                    System.out.println("Finish insert into " + tableNames.get(i));
                }
            }
        }
        if (!isTablePopulated("Temperature")) {
            processTemperatureFile("src/main/resources/data/Temperature.csv");
            System.out.println("Finish insert into temperature");
        }
    }

    private void insertCsvIntoTable(String filePath, String tableName) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/data/" + filePath))) {

            String[] columnNames = csvReader.readNext(); // Read header line
            if (columnNames == null) return;

            String sql = createInsertStatement(tableName, columnNames);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                connection.setAutoCommit(false);
                String[] data;

                while ((data = csvReader.readNext()) != null) {
                    for (int i = 0; i < data.length; i++) {
                        if (data[i].trim().isEmpty()) {
                            statement.setNull(i + 1, Types.VARCHAR);
                        } else {
                            statement.setString(i + 1, data[i]);
                        }
                    }
                    statement.addBatch();
                }

                statement.executeBatch();
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException | IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private String createInsertStatement(String tableName, String[] columnNames) {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (String col : columnNames) {
            if (columns.length() > 0) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(col.trim());
            placeholders.append("?");
        }

        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    private void processTemperatureFile(String filePath) {
        try {
            List<String[]> csvChunks = splitCsvFileIntoChunks(filePath, CHUNK_SIZE);

            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            for (String[] chunk : csvChunks) {
                executor.submit(() -> processAndInsertChunk(chunk));
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processAndInsertChunk(String[] chunk) {
        String sql = "INSERT INTO temperature (id, average_temperature, minimum_temperature, maximum_temperature, Year, city_id, country_id,  state_id, global_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = connection.createStatement();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");

            connection.setAutoCommit(false);

            for (String line : chunk) {
                String[] data = line.split(",", -1);

                for (int i = 0; i < data.length; i++) {
                    if (data[i].trim().isEmpty()) {
                        statement.setNull(i + 1, Types.VARCHAR);
                    } else {
                        statement.setString(i + 1, data[i]);
                    }
                }
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<String[]> splitCsvFileIntoChunks(String filePath, int chunkSize) throws IOException {
        List<String[]> chunks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            List<String> currentChunk = new ArrayList<>();

            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                currentChunk.add(line);
                if (++count % chunkSize == 0) {
                    chunks.add(currentChunk.toArray(new String[0]));
                    currentChunk.clear();
                }
            }

            if (!currentChunk.isEmpty()) {
                chunks.add(currentChunk.toArray(new String[0]));
            }
        }
        return chunks;
    }
    private boolean isTablePopulated(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

