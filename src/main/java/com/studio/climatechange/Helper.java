package com.studio.climatechange;
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
        List<String> csvFiles = List.of("Student.csv", "Persona.csv", "Country.csv", "City.csv", "State.csv", "Population.csv", "Global.csv", "Temp.csv");
        List<String> tableNames = List.of("Student", "Persona", "Country", "City", "State", "Population", "Global", "Temperature");

        for (int i = 0; i < csvFiles.size(); i++) {
            if (!csvFiles.get(i).equals("Temp.csv")) { // Process all files except Temp.csv
                insertCsvIntoTable(csvFiles.get(i), tableNames.get(i));
            }
        }

        // Process Temp.csv with chunk processing
        processTemperatureFile("src/main/resources/data/Temperature.csv");
    }

    private void insertCsvIntoTable(String csvFile, String tableName) {
        String filePath = "src/main/resources/data/" + csvFile;

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             BufferedReader lineReader = new BufferedReader(new FileReader(filePath))) {

            String lineText = lineReader.readLine(); // Read header line
            if (lineText == null) return;

            String[] columnNames = lineText.split(",", -1);
            String sql = createInsertStatement(tableName, columnNames);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                connection.setAutoCommit(false);

                while ((lineText = lineReader.readLine()) != null) {
                    String[] data = lineText.split(",", -1);

                    for (int i = 0; i < data.length; i++) {
                        if (data[i].trim().isEmpty()) {
                            statement.setNull(i + 1, Types.VARCHAR); // Assume VARCHAR for simplicity
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
        } catch (Exception e) {
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
        String sql = "INSERT INTO temperature (id, average_temperature, minimum_temperature, maximum_temperature, Year, country_id, city_id, state_id, global_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
}

